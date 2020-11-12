package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.IntradayRiskType;

import java.util.List;

public class IntradayConfig {
    private List<IntradayRiskType> risks;
    private int periodicity;
    private boolean modifyReversePost;              // if true, then two values are created on a trade modify event.  One value is the old value, one value is the new value
    private boolean riskOnDelete;

    public IntradayConfig() {
    }

    public IntradayConfig(List<IntradayRiskType> risks, int periodicity, boolean modifyReversePost, boolean riskOnDelete) {
        this.risks = risks;
        this.periodicity = periodicity;
        this.modifyReversePost = modifyReversePost;
        this.riskOnDelete = riskOnDelete;
    }

    public List<IntradayRiskType> getRisks() {
        return this.risks;
    }

    public int getPeriodicity() {
        return this.periodicity;
    }

    public boolean isModifyReversePost() { return modifyReversePost; }

    public boolean isRiskOnDelete() { return riskOnDelete; }
}
