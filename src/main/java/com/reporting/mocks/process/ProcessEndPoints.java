package com.reporting.mocks.process;

public class ProcessEndPoints {
    protected String calculationContextsUri;
    protected String marketEnvironmentsUri;
    protected String trade;

    public ProcessEndPoints(String pricingGroupName) {
        this.calculationContextsUri = "/CalculationContext/" + pricingGroupName + "/all";
        this.marketEnvironmentsUri = "/MarketEnvironment/" + pricingGroupName + "/all";

    }
}
