package com.reporting.mocks.process.risks;

import java.util.List;

import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.RiskType;

public class CalculationRunManifest {
    protected RiskRunType riskRunType;
    protected CalculationContextId calculationId;
    protected MarketEnvId marketEnvId;
    protected TradePopulationId tradePopulationId;
    protected List<RiskType> risksToRun;
    protected int fragmentCount;

    public CalculationRunManifest(RiskRunType riskRunType,
                          CalculationContextId calculationId,
                          MarketEnvId marketEnvId,
                          TradePopulationId tradePopulationId,
                          List<RiskType> risksToRun,
                          int fragmentCount) {
        this.riskRunType = riskRunType;
        this.calculationId = calculationId;
        this.marketEnvId = marketEnvId;
        this.tradePopulationId = tradePopulationId;
        this.risksToRun = risksToRun;
        this.fragmentCount = fragmentCount;
    }

    public RiskRunType getRiskRunType() {
        return riskRunType;
    }

    public CalculationContextId getCalculationId() {
        return calculationId;
    }

    public MarketEnvId getMarketEnvId() {
        return marketEnvId;
    }

    public TradePopulationId getTradePopulationId() {
        return tradePopulationId;
    }

    public List<RiskType> getRisksToRun() {
        return risksToRun;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }
}
