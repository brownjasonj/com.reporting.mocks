package com.reporting.mocks.process.endofday;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.IMarketStore;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeTypes.Payment;
import com.reporting.mocks.model.underlying.Underlying;
import com.reporting.mocks.process.risks.RiskRunRequest;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndofDayRiskEventProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( EndofDayRiskEventProducerThread.class.getName() );
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected IResultPublisher riskPublisher;
    protected ITradeStore tradeStore;
    protected IMarketStore marketStore;
    protected EndofDayConfig config;
    protected PricingGroup pricingGroup;
    protected CalculationContext currentCalculationContext;
    protected ICalculationContextStore calculationContextStore;
    protected int eodCounts;

    public EndofDayRiskEventProducerThread(
            PricingGroup pricingGroup,
            EndofDayConfig eodConfig,
            ITradeStore tradeStore,
            IMarketStore marketStore,
            ICalculationContextStore calculationContextStore,
            BlockingQueue<RiskRunRequest> riskRunRequestQueue,
            IResultPublisher riskPublisher) {
        this.pricingGroup = pricingGroup;
        this.marketStore = marketStore;
        this.config = eodConfig;
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = new ArrayBlockingQueue(1024 * 96);
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskPublisher = riskPublisher;
        this.calculationContextStore = calculationContextStore;
        this.eodCounts = 0;
    }

    private TradePopulation runEoDProcess(ITradeStore tradeStore, TradePopulation tradePopulation, Date asOf) {
        Map<String, Map<String,Double>> bookUnderlyingBalances = new HashMap<>();
        List<Trade> tradesToDelete = new ArrayList<>();

        for(Trade trade : tradePopulation.getAllTrades()) {
            if (trade.hasExpired(asOf)) {
                tradesToDelete.add(trade);
                Map<String,Double> balances = bookUnderlyingBalances.get(trade.getBook());
                if (balances == null) {
                    balances = new HashMap<>();
                    bookUnderlyingBalances.put(trade.getBook(), balances);
                }
                for(Map.Entry<String, Double> notional : trade.getNotionals().entrySet()) {
                    if (balances.containsKey(notional.getKey())) {
                        balances.put(notional.getKey(), balances.get(notional.getKey()) + notional.getValue());
                    }
                    else {
                        balances.put(notional.getKey(), notional.getValue());
                    }
                }
            }
        }

        for(String book : bookUnderlyingBalances.keySet()) {
            Map<String, Double> bookBalanaces = bookUnderlyingBalances.get(book);
            for(Map.Entry<String,Double> underlyingBalance : bookBalanaces.entrySet()) {
                // String book, Double underlyingAmount1, Underlying underlying1, Date settlementDate
                Payment balance = new Payment(book,
                        underlyingBalance.getValue(),
                        new Underlying(underlyingBalance.getKey()),
                        new Date());
                tradeStore.add(balance);
            }
        }

        for(Trade trade : tradesToDelete) {
            tradeStore.delete(trade.getTcn());
        }

        return this.tradeStore.create(DataMarkerType.EOD);
    }

    @Override
    public void run() {

        TimerTask eodTradePopTimerTask = new EndofDayEventTimerThread(tradeStore, this.tradePopulationIdQueue);
        //running timer task as daemon thread
        Timer tradeTimer = new Timer(true);
        tradeTimer.scheduleAtFixedRate(eodTradePopTimerTask, 0, this.config.getPeriodicity());

        try {
            while(true) {
                TradePopulationId tradePopId = this.tradePopulationIdQueue.take();
                TradePopulation tradePopulation = this.tradeStore.create(DataMarkerType.EOD);

                if (tradePopulation != null) {
                    MarketEnv market = this.marketStore.create(tradePopulation.getType(), this.eodCounts++);

                    tradePopulation = runEoDProcess(tradeStore, tradePopulation, market.getAsOf());

                    this.currentCalculationContext = this.calculationContextStore.create();
                    for(RiskType riskType : this.config.getRisks()) {
                        this.currentCalculationContext.add(riskType, market);
                    }
                    this.calculationContextStore.setCurrentContext(this.currentCalculationContext);

                    riskPublisher.publish(market);
                    riskPublisher.publish(this.currentCalculationContext);

                    this.riskRunRequestQueue.add(new RiskRunRequest(
                            RiskRunType.EndOfDay,
                            this.currentCalculationContext.getCalculationContextId(),
                            null,
                            tradePopulation.getId(),
                            this.config.getRisks(),
                            null,
                            false // this is NOT a delete event
                    ));
                }
            }
        }
        catch (InterruptedException e) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
