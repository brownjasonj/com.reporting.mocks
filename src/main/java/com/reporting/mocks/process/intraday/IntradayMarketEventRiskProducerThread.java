package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.*;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunType;
import com.reporting.mocks.process.risks.TradePopulationRiskRunRequest;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntradayMarketEventRiskProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( IntradayMarketEventRiskProducerThread.class.getName() );
    BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue;
    protected IResultPublisher riskPublisher;
    protected ITradeStore tradeStore;
    protected IMarketStore marketStore;
    protected PricingGroupConfig config;
    protected ITradePopulation tradePopulation;
    protected IntradayCalculationSchedule calculationSchedule;
    protected CalculationContext currentCalculationContext;
    protected ICalculationContextStore calculationContextStore;
    protected Map<RiskType, Set<MarketEnv>> riskMarkets;
    protected boolean run = true;

    public IntradayMarketEventRiskProducerThread(PricingGroupConfig config,
                                                 ITradeStore tradeStore,
                                                 IMarketStore IMarketStore,
                                                 ICalculationContextStore ICalculationContextStore,
                                                 BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue,
                                                 IResultPublisher riskPublisher,
                                                 MarketEnv market) {
        this.config = config;
        this.tradeStore = tradeStore;
        this.marketStore = IMarketStore;
        this.riskPublisher = riskPublisher;
        this.calculationSchedule = new IntradayCalculationSchedule();
        this.calculationContextStore = ICalculationContextStore;
        this.currentCalculationContext = ICalculationContextStore.create();
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskMarkets = new HashMap<>();
        for(IntradayRiskType riskType : config.getIntradayConfig().getRisks()) {
            this.calculationSchedule.add(riskType.getPeriodicity(), riskType.getRiskType());
            this.currentCalculationContext.add(riskType.getRiskType(), market);
            Set<MarketEnv> marketEnvSet = new HashSet<>();
            marketEnvSet.add(market);
            riskMarkets.put(riskType.getRiskType(), marketEnvSet);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Intraday Market Event Risk Producer Thread created");
        try {
            while(run) {
                Thread.sleep(config.getMarketPeriodicity());

                MarketEnv newMarket = this.marketStore.create(DataMarkerType.IND);

                // System.out.println("New Market " + newMarket.getId() + " asOf: " + newMarket.getAsOf().toString());
                // run all appropriate risks for the new trade population
                // 1. getTradeByTcn a new trade population snapshot
                // 2. run each risk on the trade population
                // 3. chunk the risks into fragments
                // 4. send to risk queue

                riskPublisher.publish(newMarket);

                // this gets and creates a new trade population
                this.tradePopulation = this.tradeStore.createSnapShot(DataMarkerType.IND);

                // increment the risk schedule since a new market arrived
                this.calculationSchedule.increment();

                List<RiskType> risksToRun = this.calculationSchedule.getRisksToRun();

                // set the markets for each of the risks to run with given market
                this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                this.currentCalculationContext = this.calculationContextStore.createCopy(this.currentCalculationContext);
                this.currentCalculationContext.update(risksToRun, newMarket);
                this.calculationContextStore.setCurrentContext(this.currentCalculationContext);

                this.riskPublisher.publish(this.currentCalculationContext);

                this.riskRunRequestQueue.add(
                        new TradePopulationRiskRunRequest(
                                RiskRunType.Intraday,
                                this.currentCalculationContext.getCalculationContextId(),
                                newMarket.getId(),
                                risksToRun,
                                this.tradePopulation.getId()
                        )
                );
            }
        } catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}