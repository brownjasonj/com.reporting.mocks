package com.reporting.mocks.model.risks;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
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

    public Pv(CalculationContextId calculationId, MarketEnvId marketEnvId, TradePopulationId tradePopulationId, RiskRunId riskRunId, String bookName, Tcn tcn, Currency currency) {
        super(calculationId, marketEnvId, tradePopulationId, riskRunId, bookName, tcn);
        Random rand = new Random();
        this.currency = currency;
        this.value = rand.nextDouble();
        this.riskType = RiskType.PV;
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

    public Pv next(RiskRunId riskRunId) {
        return new Pv(this.calculationContextId, this.marketEnvId, this.tradePopulationId, riskRunId, this.bookName, this.tcn, this.currency);
    }
}
