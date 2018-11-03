package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.risks.Risk;


public class RiskStreamFragmentState {
    protected int fragmentCount;
    protected int fragmentNo;
    protected boolean isComplete;

    public RiskStreamFragmentState(RiskRunId riskRunId, int riskCount) {
        this.fragmentCount = riskCount;
        this.fragmentNo = 0;
        this.isComplete = false;
    }


    public boolean add(Risk risk) {
        this.fragmentNo++;
        this.isComplete = (this.getFragmentNo() == this.getFragmentCount());
        return true;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }

    public int getFragmentNo() {
        return fragmentNo;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
