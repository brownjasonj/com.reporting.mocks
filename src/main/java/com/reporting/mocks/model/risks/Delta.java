package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.underlying.Currency;

import java.util.Random;
import java.util.UUID;

public class Delta extends Risk {
    protected Double value;
    protected Currency currency;

    public Delta(UUID riskRun, UUID tcn, Currency currency) {
        super(riskRun, tcn);
        this.currency = currency;
        this.value = (new Random()).nextDouble();
    }

    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    public Double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }
}
