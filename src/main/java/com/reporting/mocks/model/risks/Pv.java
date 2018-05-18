package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.underlying.Currency;

import java.util.Random;
import java.util.UUID;

public class Pv extends Risk {
    protected Double value;
    protected Currency currency;

    public Pv() {
        super();
        this.riskType = RiskType.PV;
    }

    public Pv(UUID riskRun, String bookName, UUID tcn, Currency currency) {
        super(riskRun, bookName, tcn);
        Random rand = new Random();
        this.currency = currency;
        this.value = rand.nextDouble();
    }

    public Double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public RiskType getRiskType() {
        return RiskType.PV;
    }

    public Pv next(UUID riskRun) {
        return new Pv(riskRun, this.bookName, this.tcn, this.currency);
    }
}
