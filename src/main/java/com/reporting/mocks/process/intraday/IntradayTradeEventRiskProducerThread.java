package com.reporting.mocks.process.intraday;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.generators.RiskGeneratorFactory;
import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.IMarketStore;
import com.reporting.mocks.interfaces.persistence.ITradePopulationLive;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.risks.RiskRunType;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntradayTradeEventRiskProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( IntradayTradeEventRiskProducerThread.class.getName() );
    protected BlockingQueue<TradeLifecycle> intradayEventQueue;
    protected BlockingQueue<RiskStreamMessage<? extends Risk>> riskQueue;
    protected IResultPublisher riskPublisher;
    protected ITradeStore tradeStore;
    protected IMarketStore marketStore;
    protected PricingGroupConfig config;
    protected IntradayCalculationSchedule calculationSchedule;
    protected CalculationContext currentCalculationContext;
    protected CalculationContext previousCalculationContext;
    protected ICalculationContextStore calculationContextStore;
    protected Map<RiskType, Set<MarketEnv>> riskMarkets;



    public IntradayTradeEventRiskProducerThread(PricingGroupConfig pricingGroupConfig,
                                                ITradeStore tradeStore,
                                                IMarketStore IMarketStore,
                                                ICalculationContextStore ICalculationContextStore,
                                                BlockingQueue<TradeLifecycle> intradayEventQueue,               // In queue
                                                BlockingQueue<RiskStreamMessage<? extends Risk>> riskQueue,     // out queue
                                                IResultPublisher riskPublisher,
                                                MarketEnv market) {
        this.config = pricingGroupConfig;
        this.tradeStore = tradeStore;
        this.marketStore = IMarketStore;
        this.intradayEventQueue = intradayEventQueue;
        this.riskQueue = riskQueue;
        this.riskPublisher = riskPublisher;
        this.calculationSchedule = new IntradayCalculationSchedule();
        this.calculationContextStore = ICalculationContextStore;
        this.currentCalculationContext = ICalculationContextStore.create();
        this.previousCalculationContext = this.currentCalculationContext;
        this.riskMarkets = new HashMap<>();
        for(IntradayRiskType riskType : pricingGroupConfig.getIntradayConfig().getRisks()) {
            this.calculationSchedule.add(riskType.getPeriodicity(), riskType.getRiskType());
            this.currentCalculationContext.add(riskType.getRiskType(), market);
            Set<MarketEnv> marketEnvSet = new HashSet<>();
            marketEnvSet.add(market);
            riskMarkets.put(riskType.getRiskType(), marketEnvSet);
        }
    }

    protected CalculationContext tryGettingCalculationContext(CalculationContextId calculationContextId) {
        CalculationContext calculationContext = this.calculationContextStore.getCalculationContextById(calculationContextId);
        // it is possible that the calculationContext get returns Null due to the Store not having persisted it yet.  As a
        // consequence one needs to try several times to retreive the context, if after seveal attempts then abandon
        // trying to run the calculations as something more catastrophic may have happened!
        if (calculationContext == null) {
            Boolean retreivedCC = false;
            int retryCount = 5;
            int backOffPeriodms = 500;
            while (!retreivedCC && retryCount > 0) {
                retryCount--;
                calculationContext = this.calculationContextStore.getCalculationContextById(calculationContextId);

                if (calculationContext == null) {
                    try {
                        Thread.sleep(backOffPeriodms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return calculationContext;
    }

    protected void calculateAndPublishRisk(
            CalculationContext currentCalculationContext,
            CalculationContext previousCalculationContext,
            RiskRunType riskRunType,
            Trade trade,
            boolean isDeleteEvent
    ) {
        // if the calculatonContext is still null, then return without doing anything as it is not possible to proceed
        if (currentCalculationContext == null || previousCalculationContext == null)
            return;

        List<RiskType> riskTypes = this.config.getTradeConfig().findRiskByTradeType(trade.getTradeType());

        int riskCount = 0;
        int riskNo = 0;

        // First calculate any risks for all those risks where the market changed.
        for(RiskType riskType : riskTypes) {
            MarketEnvId currentMarketEnvId = currentCalculationContext.get(riskType);
            MarketEnvId previousMarketEnvId = previousCalculationContext.get(riskType);
            if (!currentMarketEnvId.getId().toString().equals(previousMarketEnvId.getId().toString())) {
                riskCount++;
            }
        }
        RiskRunId riskRunId = new RiskRunId("riskrunid");
        for (RiskType riskType : riskTypes) {
            IRiskGeneratorLite<? extends Risk> riskGeneratorLite = RiskGeneratorFactory.getGeneratorLite(riskType);

            MarketEnvId currentMarketEnvId = currentCalculationContext.get(riskType);
            MarketEnvId previousMarketEnvId = previousCalculationContext.get(riskType);

            if (!currentMarketEnvId.getId().toString().equals(previousMarketEnvId.getId().toString())) {
                Risk risk = riskGeneratorLite.generate(previousMarketEnvId, trade);
                riskNo++;
                RiskStreamMessage<? extends Risk> riskStreamMsg = new RiskStreamMessage<>(
                        previousCalculationContext.getCalculationContextId(),
                        riskRunId,
                        riskRunType,
                        riskCount,
                        riskNo,
                        risk,
                        isDeleteEvent);
                this.riskQueue.add(riskStreamMsg);
            }
        }

        // Second calculate the risks for the new market
        riskCount = riskTypes.size();
        riskNo = 0;
        riskRunId = new RiskRunId("riskrunid");
        for (RiskType riskType : riskTypes) {
            IRiskGeneratorLite<? extends Risk> riskGeneratorLite = RiskGeneratorFactory.getGeneratorLite(riskType);
            riskNo++;
            MarketEnvId currentMarketEnvId = currentCalculationContext.get(riskType);
            Risk risk = riskGeneratorLite.generate(currentMarketEnvId, trade);
            RiskStreamMessage<? extends Risk> riskStreamMsg = new RiskStreamMessage<>(
                    currentCalculationContext.getCalculationContextId(),
                    riskRunId,
                    riskRunType,
                    riskCount,
                    riskNo,
                    risk,
                    isDeleteEvent);
            this.riskQueue.add(riskStreamMsg);
        }
    }

    @Override
    public void run() {
        try {
            ITradePopulationLive tradePopulationLive = this.tradeStore.getLiveTradePopulation();
            while(true) {
                TradeLifecycle tradeLifecycleEvent = intradayEventQueue.take();
                // grab the latest calculation context from the store as it may have changed due to an end of day event
                this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                switch (tradeLifecycleEvent.getLifecycleType()) {
                    case New: {
                        // before calculating risk, check that the trade was not in the current ITradePopulation
                        // if it is, then the risk was already calculated for it, so skip this trade
                        Trade trade = tradeLifecycleEvent.getTradeAfterLifeCycle();
                        if (trade != null) {
                            Trade existingTrade = tradePopulationLive.getTrade(trade.getTcn());
                            if (existingTrade == null) {
                                // add the trade to the current tradepopulation
                                tradePopulationLive.add(trade);
                                // calculate all the risks for this trade, since it is not in current population or has a different version
                                calculateAndPublishRisk(
                                        this.calculationContextStore.getCurrentContext(),
                                        this.calculationContextStore.getPreviousContext(),
                                        RiskRunType.Intraday,
                                        trade,
                                        false);
                            }
                        }
                    }
                    break;
                    case Modify: {
                        Trade tradeBeforeModify = tradeLifecycleEvent.getTradeBeforeLifeCycle();
                        Trade tradeAfterModify = tradeLifecycleEvent.getTradeAfterLifeCycle();
                        if (tradeBeforeModify != null && tradeAfterModify != null) {
                            Trade existingTrade = tradePopulationLive.getTrade(tradeBeforeModify.getTcn());
                            if (existingTrade == null)
                            if (existingTrade != null && tradeAfterModify.getVersion() != existingTrade.getVersion()) {
                                this.currentCalculationContext = this.calculationContextStore.getCurrentContext();
                                // modify trade in the current trade population
                                tradePopulationLive.delete(tradeBeforeModify.getTcn());

                                if (this.config.getIntradayConfig().isModifyReversePost()) {
                                    calculateAndPublishRisk(
                                            this.calculationContextStore.getCurrentContext(),
                                            this.calculationContextStore.getPreviousContext(),
                                            RiskRunType.Intraday,
                                            tradeBeforeModify,
                                            true);
                                }
                                tradePopulationLive.add(tradeAfterModify);
                                // calculate all the risks for this trade, since it is not in current population or has a different version
                                calculateAndPublishRisk(
                                        this.calculationContextStore.getCurrentContext(),
                                        this.calculationContextStore.getPreviousContext(),
                                        RiskRunType.Intraday,
                                        tradeBeforeModify,
                                        false);

                            }
                        }
                    }
                    break;
                    case Delete: {
                        // send something????
                        Trade tradeBeforeDelete = tradeLifecycleEvent.getTradeBeforeLifeCycle();
                        if (tradeBeforeDelete != null) {
                            Trade existingTrade = tradePopulationLive.getTrade(tradeBeforeDelete.getTcn());
                            if (existingTrade != null) {
                                tradePopulationLive.delete(tradeBeforeDelete.getTcn());
                                if (this.config.getIntradayConfig().isRiskOnDelete()) {
                                    calculateAndPublishRisk(
                                            this.calculationContextStore.getCurrentContext(),
                                            this.calculationContextStore.getPreviousContext(),
                                            RiskRunType.Intraday,
                                            tradeBeforeDelete,
                                            true);
                                }
                            }
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }
        catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}