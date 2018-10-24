package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultStore;
import com.reporting.mocks.persistence.ITradeStore;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamRiskResultPublisherThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( StreamRiskResultPublisherThread.class.getName() );
    protected BlockingQueue<RiskStreamMessage> riskQueue;
    protected PricingGroupConfig appConfig;
    protected RiskRunPublisher riskRunPublisher;
    protected IRiskResultStore riskResultStore;
    protected Map<UUID, RiskStreamFragmentState> riskStreams;


    public StreamRiskResultPublisherThread(
            BlockingQueue<RiskStreamMessage> riskQueue,
            PricingGroupConfig appConfig,
            RiskRunPublisher riskRunPublisher,
            IRiskResultStore riskResultStore
    ) {
        this.riskQueue = riskQueue;
        this.appConfig = appConfig;
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
                    riskStream = new RiskStreamFragmentState(riskStreamMsg.riskRunId, riskStreamMsg.riskCount, this.appConfig.getRiskResultsPerFragment());
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
                        RiskResult riskResult = new RiskResult(
                                riskStreamMsg.getCalculationContextId(),
                                riskStreamMsg.getTradePopulationId(),
                                riskStreamMsg.getRiskRunId(),
                                riskStream.getFragmentCount(),
                                riskStream.getFragmentNo(),
                                riskStream.getRisks(),
                                riskStreamMsg.isDelete());

                        // persist the riskResult for future use
                        this.riskResultStore.add(riskResult);

                        switch (riskStreamMsg.getRiskRunType()) {
                            case EndOfDay:
                                riskRunPublisher.publishEndofDayRiskRun(riskResult);
                                break;
                            case OnDemand:
                            case Intraday:
                                riskRunPublisher.publishIntradayRiskRun(riskResult);
                                break;
                            case IntradayTick:
                                riskRunPublisher.publishIntradayTick(riskResult);
                                break;
                            default:
                        }
                        riskStream.nextFragment();
                    }
                }
            }
        }
        catch (InterruptedException ie) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }

    }
}
