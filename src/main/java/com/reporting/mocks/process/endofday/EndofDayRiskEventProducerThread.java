package com.reporting.mocks.process.endofday;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.CalculationContextStore;
import com.reporting.mocks.persistence.MarketStore;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.risks.RiskResult;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EndofDayRiskEventProducerThread implements Runnable {
    protected BlockingQueue<TradePopulationId> tradePopulationIdQueue;
    protected RiskRunPublisher riskPublisher;
    protected TradeStore tradeStore;
    protected MarketStore marketStore;
    protected EndofDayConfig config;
    protected PricingGroup pricingGroup;
    protected CalculationContext currentCalculationContext;
    protected CalculationContextStore calculationContextStore;

    public EndofDayRiskEventProducerThread(
            PricingGroup pricingGroup,
            EndofDayConfig eodConfig,
            TradeStore tradeStore,
            MarketStore marketStore,
            CalculationContextStore calculationContextStore,
            RiskRunPublisher riskPublisher) {
        this.pricingGroup = pricingGroup;
        this.marketStore = marketStore;
        this.config = eodConfig;
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = new ArrayBlockingQueue(1024);;
        this.riskPublisher = riskPublisher;
        this.calculationContextStore = calculationContextStore;
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
                TradePopulation tradePopulation = this.tradeStore.get(tradePopId.getId());

                if (tradePopulation != null) {
                    MarketEnv market = this.marketStore.create(tradePopulation.getType());
                    this.currentCalculationContext = this.calculationContextStore.create();
                    for(RiskType riskType : this.config.getRisks()) {
                        this.currentCalculationContext.add(riskType, market);
                    }

                    riskPublisher.publish(market);
                    riskPublisher.publish(this.currentCalculationContext);

                    List<RiskResult> results = RiskRunGenerator.generate(
                            currentCalculationContext,
                            tradePopulation,
                            this.config.getRisks(),
                            20);

                    for(RiskResult r : results) {
                        riskPublisher.publish(r);
                    }
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
