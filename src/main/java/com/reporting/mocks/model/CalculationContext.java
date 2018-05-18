package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;

import java.util.*;

/*
    A CalculationContext represents a set of associations between a risk type and a market environment.
    The association is used to determine which market environment should be used to calculate the specific
    risk.
 */
public class CalculationContext {
    UUID id;
    Date timeStamp;
    PricingGroup pricingGroup;
    Map<RiskType, MarketEnv> markets;

    public CalculationContext(PricingGroup pricingGroup) {
        this.id = UUID.randomUUID();
        this.timeStamp = new Date();
        this.pricingGroup = pricingGroup;
        this.markets = new HashMap<>();
    }

    public CalculationContext(CalculationContext calcContext) {
        this(calcContext.pricingGroup);
        this.markets = new HashMap<>(calcContext.markets);
    }

    public void update(List<RiskType> riskTypes, MarketEnv marketEnv) {
        for(RiskType riskType : riskTypes) {
            markets.put(riskType, marketEnv);
        }
    }

    public void add(RiskType riskType, MarketEnv marketEnv) {
        this.markets.put(riskType, marketEnv);
    }

    public MarketEnv get(RiskType riskType) {
        return markets.get(riskType);

    }

    public UUID getId() {
        return id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    public Map<RiskType, MarketEnv> getMarkets() {
        return markets;
    }
}
