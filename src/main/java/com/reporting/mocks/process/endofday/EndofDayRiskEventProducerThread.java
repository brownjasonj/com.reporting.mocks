package com.reporting.mocks.process.endofday;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.risks.RiskRunType;
import com.reporting.mocks.process.risks.requests.MTSRRiskRunRequest;
import com.reporting.mocks.process.risks.response.RiskRunResult;

import javax.lang.model.type.PrimitiveType;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EndofDayRiskEventProducerThread implements Runnable {
    protected BlockingQueue<UUID> tradePopulationIdQueue;
    protected RiskRunPublisher riskPublisher;
    protected TradeStore tradeStore;
    protected EndofDayConfig config;
    protected PricingGroup pricingGroup;

    public EndofDayRiskEventProducerThread(PricingGroupConfig config, TradeStore tradeStore, RiskRunPublisher riskPublisher) {
        this.pricingGroup = config.getPricingGroupId();
        this.config = config.getEndofdayConfig();
        this.tradeStore = tradeStore;
        this.tradePopulationIdQueue = new ArrayBlockingQueue(1024);;
        this.riskPublisher = riskPublisher;
    }

    @Override
    public void run() {

        TimerTask eodTradePopTimerTask = new EndofDayEventTimerThread(tradeStore, this.tradePopulationIdQueue);
        //running timer task as daemon thread
        Timer tradeTimer = new Timer(true);
        tradeTimer.scheduleAtFixedRate(eodTradePopTimerTask, 0, this.config.getPeriodicity());

        try {
            while(true) {
                UUID tradePopId = this.tradePopulationIdQueue.take();
                TradePopulation tradePopulation = this.tradeStore.getTradePopulation(tradePopId);

                if (tradePopulation != null) {
                    MarketEnv market = new MarketEnv(this.pricingGroup, tradePopulation.getType());
                    for (RiskType risk : this.config.getRisks()) {
                        MTSRRiskRunRequest riskRunRequest = new MTSRRiskRunRequest(RiskRunType.EndOfDay, market, tradePopulation, risk, 20);
                        List<RiskRunResult> results = RiskRunGenerator.generate(tradePopulation, riskRunRequest);
                        for(RiskRunResult r : results) {
                            riskPublisher.send(r);
                        }
                    }
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
