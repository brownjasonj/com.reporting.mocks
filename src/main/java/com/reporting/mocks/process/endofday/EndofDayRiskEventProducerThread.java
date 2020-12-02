package com.reporting.mocks.process.endofday;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.interfaces.persistence.*;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.DataMarkerType;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeTypes.Payment;
import com.reporting.mocks.model.underlying.Underlying;
import com.reporting.mocks.process.risks.TradePopulationRiskRunRequest;
import com.reporting.mocks.process.risks.RiskRunType;

public class EndofDayRiskEventProducerThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger( EndofDayRiskEventProducerThread.class.getName() );
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue;
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
            BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue,
            IResultPublisher riskPublisher) {
        this.pricingGroup = pricingGroup;
        this.marketStore = marketStore;
        this.config = eodConfig;
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = new LinkedBlockingDeque<>();
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskPublisher = riskPublisher;
        this.calculationContextStore = calculationContextStore;
        this.eodCounts = 0;
    }

    private ITradePopulation runEoDProcess(ITradeStore tradeStore, ITradePopulation tradePopulation, Instant asOf) {
        Map<String, Map<String,Double>> bookUnderlyingBalances = new HashMap<>();
        List<Trade> tradesToDelete = new ArrayList<>();

        ITradePopulationLive tradePopulationLive = tradeStore.getLiveTradePopulation();

        for(Trade trade : tradePopulation.getTrades()) {
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
                        Instant.now(Clock.system(ZoneOffset.UTC)));
                tradePopulationLive.add(balance);
            }
        }

        for(Trade trade : tradesToDelete) {
            tradePopulationLive.delete(trade.getTcn());
        }

        return this.tradeStore.createSnapShot(DataMarkerType.EOD);
    }

    @Override
    public void run() {

        TimerTask eodTradePopTimerTask = new EndofDayEventTimerThread(tradeStore, this.tradePopulationIdQueue);
        //running timer task as daemon thread
        Timer tradeTimer = new Timer(true);
        tradeTimer.scheduleAtFixedRate(eodTradePopTimerTask, 0, this.config.getPeriodicity());

        try {
            while (true) {

                TradePopulationId tradePopulationId = this.tradePopulationIdQueue.take();

                ITradePopulation tradePopulation = this.tradeStore.getTradePopulationById(tradePopulationId);

                if (tradePopulation != null) {
                    MarketEnv market = this.marketStore.create(tradePopulation.getType(), this.eodCounts++);

                    tradePopulation = runEoDProcess(tradeStore, tradePopulation, market.getAsOf());

                    this.currentCalculationContext = this.calculationContextStore.create();
                    this.currentCalculationContext.update(this.config.getRisks(), market);
                    this.calculationContextStore.setCurrentContext(this.currentCalculationContext);

                    riskPublisher.publish(market);
                    riskPublisher.publish(this.currentCalculationContext);

                    this.riskRunRequestQueue.add(new TradePopulationRiskRunRequest(
                            RiskRunType.EndOfDay,
                            this.currentCalculationContext.getCalculationContextId(),
                            this.config.getRisks(),
                            tradePopulation.getId()
                    ));
                }
            }
        }
        catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log( Level.FINE, "thread interrupted");
        }

    }
}
