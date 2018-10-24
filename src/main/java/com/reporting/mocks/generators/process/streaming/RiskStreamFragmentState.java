package com.reporting.mocks.generators.process.streaming;

import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.risks.Risk;

import java.util.ArrayList;
import java.util.List;

public class RiskStreamFragmentState {
    protected int totalRiskCount;
    protected int fragmentCount;
    protected int fragmentNo;
    protected List<Risk> riskSet;
    protected int fragmentSize;
    protected boolean isComplete;

    public RiskStreamFragmentState(RiskRunId riskRunId, int riskCount, int fragmentSize) {
        this.riskSet = new ArrayList<>();
        this.fragmentCount = (riskCount / fragmentSize) + ((riskCount % fragmentSize) > 0 ? 1 : 0);
        this.fragmentNo = fragmentCount;
        this.totalRiskCount = riskCount;
        this.fragmentSize = fragmentSize;
        this.isComplete = false;
    }


    public boolean add(Risk risk) {
        this.riskSet.add(risk);
        this.totalRiskCount--;
        if ((this.riskSet.size() == this.fragmentSize) || (this.fragmentNo == 1 && this.totalRiskCount == 0)) {
            if (this.totalRiskCount == 0)
                this.isComplete = true;
            return true;
        }
        else {
            return false;
        }
    }

    public List<Risk> getRisks() {
        return this.riskSet;
    }

    public void nextFragment() {
        this.riskSet.clear();
        this.fragmentNo--;
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
