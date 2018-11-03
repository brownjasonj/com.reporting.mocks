package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.IResultPublisher;
import com.reporting.mocks.model.RiskResultSet;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.persistence.IRiskResultSetStore;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamRiskResultSetPublisherThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( StreamRiskResultSetPublisherThread.class.getName() );
    protected BlockingQueue<RiskStreamMessage> riskQueue;
    protected PricingGroupConfig appConfig;
    protected IResultPublisher riskResultSetPublisher;
    protected IRiskResultSetStore riskResultStore;
    protected Map<UUID, RiskResultStreamFragmentState> riskStreams;


    public StreamRiskResultSetPublisherThread(
            BlockingQueue<RiskStreamMessage> riskQueue,
            PricingGroupConfig appConfig,
            IResultPublisher riskResultSetPublisher,
            IRiskResultSetStore riskResultStore
    ) {
        this.riskQueue = riskQueue;
        this.appConfig = appConfig;
        this.riskResultSetPublisher = riskResultSetPublisher;
        this.riskResultStore = riskResultStore;
        this.riskStreams = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                RiskStreamMessage<? extends Risk> riskStreamMsg = this.riskQueue.take();
                UUID riskRunId = riskStreamMsg.riskRunId.getId();
                RiskResultStreamFragmentState riskStream = null;
                if (!this.riskStreams.containsKey(riskRunId)) {
                    riskStream = new RiskResultStreamFragmentState(riskStreamMsg.riskRunId, riskStreamMsg.riskCount, this.appConfig.getRiskResultsPerFragment());
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
                        RiskResultSet riskResultSet = new RiskResultSet(
                                riskStreamMsg.getCalculationContextId(),
                                riskStreamMsg.getTradePopulationId(),
                                riskStreamMsg.getRiskRunId(),
                                riskStream.getFragmentCount(),
                                riskStream.getFragmentNo(),
                                riskStream.getRisks(),
                                riskStreamMsg.isDelete());

                        // persist the riskResultSet for future use
                        this.riskResultStore.add(riskResultSet);

                        switch (riskStreamMsg.getRiskRunType()) {
                            case EndOfDay:
                                riskResultSetPublisher.publishEndofDayRiskRun(riskResultSet);
                                break;
                            case OnDemand:
                            case Intraday:
                                riskResultSetPublisher.publishIntradayRiskResultSet(riskResultSet);
                                break;
                            case IntradayTick:
                                riskResultSetPublisher.publishIntradayRiskResultSet(riskResultSet);
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
