package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.trade.Tcn;

import java.util.UUID;

public abstract class Risk {
    protected String bookName;
    protected Tcn tcn;
    protected UUID riskRun;
    protected RiskType riskType;

    protected Risk() {

    }

    protected Risk(UUID riskRun, String bookName, Tcn tcn) {
        this.bookName = bookName;
        this.riskRun = riskRun;
        this.tcn = tcn;
    }

    public RiskType getRiskType() {
        return this.riskType;
    }

    public Tcn getTcn() {
        return tcn;
    }

    public UUID getRiskRun() {
        return riskRun;
    }

    public String getBookName() {
        return bookName;
    }
}
