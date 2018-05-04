package com.reporting.mocks.endpoints.JavaQueue;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.response.SRRunResponse;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RiskRunResultQueuePublisher implements RiskRunPublisher {
    protected BlockingQueue<MRRunResponse> mrRisk;
    protected BlockingQueue<SRRunResponse> srRisk;

    RiskRunResultQueuePublisher() {
        this.mrRisk = new ArrayBlockingQueue<>(4096);
        this.srRisk = new ArrayBlockingQueue<>(4096);
    }

    @Override
    public void send(RiskRunResult riskRunResult) {
        try {
            switch (riskRunResult.getSetKind()) {
                case MR:
                    mrRisk.put((MRRunResponse)riskRunResult);
                    break;
                case SR:
                    srRisk.put((SRRunResponse)riskRunResult);
                    break;
                default:
                    break;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
