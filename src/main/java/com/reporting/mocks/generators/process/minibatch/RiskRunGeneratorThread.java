package com.reporting.mocks.generators.process.minibatch;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.IResultPublisher;
import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.generators.RiskGeneratorFactory;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultSetStore;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.process.endofday.EndofDayEventTimerThread;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RiskRunGeneratorThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( EndofDayEventTimerThread.class.getName() );
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected BlockingQueue<Risk> riskQueue;
    protected ICalculationContextStore calculationContextStore;
    protected ITradeStore tradeStore;
    protected IResultPublisher resultPublisher;
    protected IRiskResultSetStore riskResultStore;
    protected PricingGroupConfig appConfig;

    protected int fragmentCount;
    protected int fragmentNo;
    protected int riskCount;
    protected RiskRunRequest riskRunRequest;

    public RiskRunGeneratorThread(BlockingQueue<RiskRunRequest> riskRunRequestQueue,
                                  PricingGroupConfig appConfig,
                                  ICalculationContextStore ICalculationContextStore,
                                  ITradeStore tradeStore,
                                  IResultPublisher resultPublisher,
                                  IRiskResultSetStore riskResultStore
                                  ) {
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.calculationContextStore = ICalculationContextStore;
        this.tradeStore = tradeStore;
        this.riskResultStore = riskResultStore;
        this.resultPublisher = resultPublisher;
        this.appConfig = appConfig;
    }

    private void publishRisk(List<Risk> risks, Risk risk) {
        risks.add(risk);

        if ((risks.size() == this.appConfig.getRiskResultsPerFragment()) || (this.fragmentCount == 1 && riskCount == 0)) {
            RiskResultSet riskResultSet = new RiskResultSet(
                    riskRunRequest.getCalculationId(),
                    riskRunRequest.getTradePopulationId(),
                    riskRunRequest.getRiskRunId(),
                    this.fragmentCount,
                    this.fragmentNo,
                    risks,
                    riskRunRequest.isDeleteEvent());

            this.fragmentCount--;

            // persist the riskResultSet for future use
            this.riskResultStore.add(riskResultSet);

            switch (riskRunRequest.getRiskRunType()) {
                case EndOfDay:
                    resultPublisher.publishEndofDayRiskRun(riskResultSet);
                    break;
                case OnDemand:
                case Intraday:
                    resultPublisher.publishIntradayRiskResultSet(riskResultSet);
                    break;
                case IntradayTick:
                    resultPublisher.publishIntradayRiskResultSet(riskResultSet);
                    break;
                default:
            }
            risks.clear();
        }
    }

    protected void processBulkRiskRequest(RiskRunRequest riskRunRequest) {
        TradePopulationId tradePopulationId = riskRunRequest.getTradePopulationId();
        TradePopulation tradePopulation = this.tradeStore.getTradePopulation(tradePopulationId);
        Map<TradeType, List<Trade>> tradeTypeToTradeMapping = tradePopulation.tradeTypeToTradeMapping();
        CalculationContext calculationContext = this.calculationContextStore.get(riskRunRequest.getCalculationId().getId());

        RiskRequest riskRequest = new RiskRequest(calculationContext, tradePopulationId);

        List<Risk> risks = new ArrayList<>();

        // calculate the number of risk results that will be generate the formula is
        //
        // RiskResultCount = count(TradeType) * (count(Risks(TradeType)) * count(Trades(TradeType)))
        this.riskCount = 0;
        for(Map.Entry<TradeType, List<Trade>> entry : tradeTypeToTradeMapping.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                this.riskCount += this.appConfig.getTradeConfig().findRiskByTradeType(entry.getKey()).size() * entry.getValue().size();
            }
        }

        int risksPerFragment = this.appConfig.getRiskResultsPerFragment();
        this.fragmentCount = (riskCount / risksPerFragment) + ((riskCount % risksPerFragment) > 0 ? 1 : 0);
        this.fragmentNo = fragmentCount;

        for(TradeType tradeType : tradePopulation.getAllTradeTypes()) {

            for(RiskType riskType : this.appConfig.getTradeConfig().findRiskByTradeType(tradeType)) {
                IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);

                for(Trade trade : tradePopulation.getByTradeType(tradeType)) {
                    Risk risk = riskGenerator.generate(riskRequest, trade);
                    this.riskCount--;
                    this.publishRisk(risks, risk);
                }
            }
        }
    }

    protected void processSingleTradeRiskRunRequest(RiskRunRequest riskRunRequest) {
        if (riskRunRequest.isSingleTrade()) {
            TradePopulationId tradePopulationId = riskRunRequest.getTradePopulationId();
            CalculationContext calculationContext = this.calculationContextStore.get(riskRunRequest.getCalculationId().getId());
            Trade trade = riskRunRequest.getTrade();
            RiskRequest riskRequest = new RiskRequest(calculationContext, tradePopulationId);
            List<Risk> risks = new ArrayList<>();

            this.riskCount = this.appConfig.getTradeConfig().findRiskByTradeType(trade.getTradeType()).size();
            int risksPerFragment = this.appConfig.getRiskResultsPerFragment();
            this.fragmentCount = (riskCount / risksPerFragment) + ((riskCount % risksPerFragment) > 0 ? 1 : 0);
            this.fragmentNo = fragmentCount;

            for (RiskType riskType : this.appConfig.getTradeConfig().findRiskByTradeType(trade.getTradeType())) {
                IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
                Risk risk = riskGenerator.generate(riskRequest, trade);
                this.riskCount--;
                this.publishRisk(risks, risk);
            }
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                riskRunRequest = this.riskRunRequestQueue.take();
                if (riskRunRequest.isSingleTrade()) {
                    processSingleTradeRiskRunRequest(riskRunRequest);
                }
                else {
                    this.processBulkRiskRequest(riskRunRequest);
                }
            }
        }
        catch (InterruptedException ie) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }
    }
}
