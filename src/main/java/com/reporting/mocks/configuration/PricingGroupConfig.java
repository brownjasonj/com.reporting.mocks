package com.reporting.mocks.configuration;

import com.reporting.mocks.model.PricingGroup;

public class PricingGroupConfig {
    protected PricingGroup pricingGroupId;
    protected TradeConfig tradeConfig;
    protected IntradayConfig intradayConfig;
    protected EndofDayConfig endofdayConfig;

    protected boolean eod = true;
    protected boolean sod = true;
    protected boolean ind = true;

    protected int marketPeriodicity = 5 * 1000;   // milliseconds between change in market data.

    public PricingGroupConfig() {
    }

    public PricingGroupConfig(PricingGroup pricingGroupId, TradeConfig tradeConfig, EndofDayConfig eodc, IntradayConfig indc) {
        this();
        this.pricingGroupId = pricingGroupId;
        this.tradeConfig = tradeConfig;
        this.intradayConfig = indc;
        this.endofdayConfig = eodc;
    }

    public PricingGroup getPricingGroupId() {
        return pricingGroupId;
    }

    public EndofDayConfig getEndofdayConfig() {
        return endofdayConfig;
    }

    public TradeConfig getTradeConfig() {
        return tradeConfig;
    }

    public IntradayConfig getIntradayConfig() {
        return intradayConfig;
    }

    public EndofDayConfig getEndofDayConfig() {
        return endofdayConfig;
    }

    public boolean isEod() {
        return eod;
    }

    public boolean isSod() {
        return sod;
    }

    public boolean isInd() {
        return ind;
    }

    public int getMarketPeriodicity() {
        return marketPeriodicity;
    }
}
