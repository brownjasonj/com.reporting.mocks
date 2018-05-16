package com.reporting.mocks.model.dataviews.book;

public class TcnRiskAggregate {
    protected Double Pv;
    protected Double Delta;

    public TcnRiskAggregate() {
        this.Pv = 0.0;
        this.Delta = 0.0;
    }

    public Double getPv() {
        return Pv;
    }

    public Double getDelta() {
        return Delta;
    }
}
