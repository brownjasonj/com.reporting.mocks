package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;

import java.util.HashMap;
import java.util.Map;

/*
    A CalculationContext represents a set of associations between a risk type and a market environment.
    The association is used to determine which market environment should be used to calculate the specific
    risk.
 */
public class CalculationContext {
    PricingGroup pricingGroup;
    Map<RiskType, MarketEnv> markets;

    public CalculationContext(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        this.markets = new HashMap<>();
    }

    public void add(RiskType riskType, MarketEnv marketEnv) {
        this.markets.put(riskType, marketEnv);
    }

    public MarketEnv get(RiskType riskType) {
        return null;

    }

}
