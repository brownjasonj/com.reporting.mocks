package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.generators.RiskGeneratorFactory;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultStore;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamRiskRunGeneratorThread implements Runnable  {
    private static final Logger LOGGER = Logger.getLogger( StreamRiskRunGeneratorThread.class.getName() );
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected BlockingQueue<RiskStreamMessage> riskQueue;
    protected ICalculationContextStore calculationContextStore;
    protected ITradeStore tradeStore;
    protected RiskRunPublisher riskRunPublisher;
    protected IRiskResultStore riskResultStore;
    protected PricingGroupConfig appConfig;


    public StreamRiskRunGeneratorThread(BlockingQueue<RiskRunRequest> riskRunRequestQueue,
                                        BlockingQueue<RiskStreamMessage> riskStreamQueue,
                                        PricingGroupConfig appConfig,
                                        ICalculationContextStore ICalculationContextStore,
                                        ITradeStore tradeStore,
                                        RiskRunPublisher riskRunPublisher,
                                        IRiskResultStore riskResultStore) {
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.riskQueue = riskStreamQueue;
        this.calculationContextStore = ICalculationContextStore;
        this.tradeStore = tradeStore;
        this.riskResultStore = riskResultStore;
        this.riskRunPublisher = riskRunPublisher;
        this.appConfig = appConfig;
    }

    protected void processBulkRiskRequest(RiskRunRequest riskRunRequest) {
        TradePopulationId tradePopulationId = riskRunRequest.getTradePopulationId();
        TradePopulation tradePopulation = this.tradeStore.getTradePopulation(tradePopulationId);
        Map<TradeType, List<Trade>> tradeTypeToTradeMapping = tradePopulation.tradeTypeToTradeMapping();
        CalculationContext calculationContext = this.calculationContextStore.get(riskRunRequest.getCalculationId().getId());

        RiskRequest riskRequest = new RiskRequest(calculationContext, tradePopulationId);

        // calculate the number of risk results that will be generate the formula is
        //
        // RiskResultCount = count(TradeType) * (count(Risks(TradeType)) * count(Trades(TradeType)))
        int riskCount = 0;
        for(Map.Entry<TradeType, List<Trade>> entry : tradeTypeToTradeMapping.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                riskCount += this.appConfig.getTradeConfig().findRiskByTradeType(entry.getKey()).size() * entry.getValue().size();
            }
        }

        int riskNo = 0;
        for(TradeType tradeType : tradePopulation.getAllTradeTypes()) {

            for(RiskType riskType : this.appConfig.getTradeConfig().findRiskByTradeType(tradeType)) {
                IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);

                for(Trade trade : tradePopulation.getByTradeType(tradeType)) {
                    riskNo++;
                    Risk risk = riskGenerator.generate(riskRequest, trade);
                    RiskStreamMessage riskStreamMsg = new RiskStreamMessage(calculationContext.getCalculationContextId(),
                                                                tradePopulationId,
                                                                riskRunRequest.getRiskRunId(),
                                                                riskRunRequest.getRiskRunType(),
                                                                riskCount,
                                                                riskNo,
                                                                risk,
                                                                riskRunRequest.isDeleteEvent());
                    this.riskQueue.add(riskStreamMsg);
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

            int riskCount = this.appConfig.getTradeConfig().findRiskByTradeType(trade.getTradeType()).size();
            int riskNo = 0;

            for (RiskType riskType : this.appConfig.getTradeConfig().findRiskByTradeType(trade.getTradeType())) {
                IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
                riskNo++;
                Risk risk = riskGenerator.generate(riskRequest, trade);
                RiskStreamMessage riskStreamMsg = new RiskStreamMessage(calculationContext.getCalculationContextId(),
                        tradePopulationId,
                        riskRunRequest.getRiskRunId(),
                        riskRunRequest.getRiskRunType(),
                        riskCount,
                        riskNo,
                        risk,
                        riskRunRequest.isDeleteEvent());
                this.riskQueue.add(riskStreamMsg);
            }
        }
    }
    @Override
    public void run() {
        LOGGER.log( Level.FINE, "thread interrupted");
        try {
            while (true) {
                RiskRunRequest riskRunRequest = this.riskRunRequestQueue.take();
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
