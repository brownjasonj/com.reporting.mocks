package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.underlying.Currency;
import com.reporting.mocks.model.underlying.Underlying;

import java.util.Random;
import java.util.UUID;

public class Delta extends Risk {
    protected Double value;
    protected Underlying underlying;

    public Delta() {
        super();
        this.riskType = RiskType.DELTA;
    }

    public Delta(UUID riskRun, String bookName, Tcn tcn, Underlying currency) {
        super(riskRun, bookName, tcn);
        this.underlying = currency;
        this.value = (new Random()).nextDouble();
    }

    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    public Double getValue() {
        return value;
    }

    public Underlying getUnderlying() {
        return underlying;
    }
}
