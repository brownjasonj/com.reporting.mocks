package com.reporting.mocks.model.risks;

import java.util.UUID;

public abstract class Risk {
    protected String bookName;
    protected UUID tcn;
    protected UUID riskRun;
    protected RiskType riskType;

    protected Risk() {

    }

    protected Risk(UUID riskRun, String bookName, UUID tcn) {
        this.bookName = bookName;
        this.riskRun = riskRun;
        this.tcn = tcn;
    }

    public RiskType getRiskType() {
        return this.riskType;
    }

    public UUID getTcn() {
        return tcn;
    }

    public UUID getRiskRun() {
        return riskRun;
    }

    public String getBookName() {
        return bookName;
    }
}
