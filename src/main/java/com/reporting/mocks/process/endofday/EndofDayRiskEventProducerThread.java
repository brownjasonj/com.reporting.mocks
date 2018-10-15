package com.reporting.mocks.process.endofday;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IMarketStore;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.process.risks.RiskRunRequest;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EndofDayRiskEventProducerThread implements Runnable {
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected RiskRunPublisher riskPublisher;
    protected ITradeStore tradeStore;
    protected IMarketStore IMarketStore;
    protected EndofDayConfig config;
    protected PricingGroup pricingGroup;
    protected CalculationContext currentCalculationContext;
    protected ICalculationContextStore ICalculationContextStore;

    public EndofDayRiskEventProducerThread(
            PricingGroup pricingGroup,
            EndofDayConfig eodConfig,
            ITradeStore tradeStore,
            IMarketStore IMarketStore,
            ICalculationContextStore ICalculationContextStore,
            BlockingQueue<RiskRunRequest> riskRunRequestQueue,
            RiskRunPublisher riskPublisher) {
        this.pricingGroup = pricingGroup;
        this.IMarketStore = IMarketStore;
        this.config = eodConfig;
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = new ArrayBlockingQueue(1024);
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskPublisher = riskPublisher;
        this.ICalculationContextStore = ICalculationContextStore;
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
                TradePopulation tradePopulation = this.tradeStore.getTradePopulation(tradePopId);

                if (tradePopulation != null) {
                    MarketEnv market = this.IMarketStore.create(tradePopulation.getType());
                    this.currentCalculationContext = this.ICalculationContextStore.create();
                    for(RiskType riskType : this.config.getRisks()) {
                        this.currentCalculationContext.add(riskType, market);
                    }

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
            e.printStackTrace();
        }
    }
}
