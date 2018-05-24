package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
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

    public Delta(CalculationContextId calculationId, MarketEnvId marketEnvId, TradePopulationId tradePopulationId, RiskRunId riskRunId,  String bookName, Tcn tcn, Underlying currency) {
        super(calculationId, marketEnvId, tradePopulationId, riskRunId, bookName, tcn);
        this.underlying = currency;
        this.value = (new Random()).nextDouble();
        this.riskType = RiskType.DELTA;
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
