package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.IResultPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultStore;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamRiskResultPublisherThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( StreamRiskResultSetPublisherThread.class.getName() );
    protected BlockingQueue<RiskStreamMessage> riskQueue;
    protected PricingGroupConfig appConfig;
    protected ICalculationContextStore calculationContextStore;
    protected IResultPublisher riskRunPublisher;
    protected IRiskResultStore riskResultStore;
    protected Map<UUID, RiskStreamFragmentState> riskStreams;


    public StreamRiskResultPublisherThread(
            BlockingQueue<RiskStreamMessage> riskQueue,
            PricingGroupConfig appConfig,
            ICalculationContextStore calculationContextStore,
            IResultPublisher riskRunPublisher,
            IRiskResultStore riskResultStore
    ) {
        this.riskQueue = riskQueue;
        this.appConfig = appConfig;
        this.calculationContextStore = calculationContextStore;
        this.riskRunPublisher = riskRunPublisher;
        this.riskResultStore = riskResultStore;
        this.riskStreams = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                RiskStreamMessage<? extends Risk> riskStreamMsg = this.riskQueue.take();
                UUID riskRunId = riskStreamMsg.riskRunId.getId();
                RiskStreamFragmentState riskStream = null;
                if (!this.riskStreams.containsKey(riskRunId)) {
                    riskStream = new RiskStreamFragmentState(riskStreamMsg.riskRunId, riskStreamMsg.riskCount);
                    this.riskStreams.put(riskRunId, riskStream);
                }
                else {
                    riskStream = this.riskStreams.get(riskRunId);
                }

                if (riskStream.isComplete) {
                    LOGGER.log( Level.FINE, "riskRunId" + riskStreamMsg.getRiskRunId() + " is complete");
                }
                else {
                    if (riskStream.add(riskStreamMsg.getRisk())) {
                        // get the market that risk was calculated againsts
                        CalculationContext calculationContext = this.calculationContextStore.get(riskStreamMsg.calculationContextId.getId());
                        MarketEnvId marketEnvId = calculationContext.get(riskStreamMsg.getRisk().getRiskType());

                        RiskResult riskResult = new RiskResult(
                                riskStreamMsg.getRiskRunId(),
                                marketEnvId,
                                riskStreamMsg.getTradePopulationId(),
                                riskStream.getFragmentCount(),
                                riskStream.getFragmentNo(),
                                riskStreamMsg.getRisk(),
                                riskStreamMsg.isDelete()
                        );

                        // persist the riskResultSet for future use
                        this.riskResultStore.add(riskResult);

                        switch (riskStreamMsg.getRiskRunType()) {
                            case EndOfDay:
                                riskRunPublisher.publishIntradayRiskResult(riskResult);
                                break;
                            case OnDemand:
                            case Intraday:
                                riskRunPublisher.publishIntradayRiskResult(riskResult);
                                break;
                            case IntradayTick:
                                riskRunPublisher.publishIntradayRiskResult(riskResult);
                                break;
                            default:
                        }
                    }
                }
            }
        }
        catch (InterruptedException ie) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }

    }
}
