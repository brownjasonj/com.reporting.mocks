package com.reporting.mocks.configuration;

public class PricingGroupConfig {
    protected String name;
    protected TradeConfig tradeConfig;
    protected IntradayConfig intradayConfig;
    protected EndofDayConfig endofdayConfig;

    protected boolean eod = true;
    protected boolean sod = true;
    protected boolean ind = true;

    protected int marketPeriodicity = 15 * 60 * 1000;   // milliseconds between change in market data.

    public PricingGroupConfig() {
    }

    public PricingGroupConfig(String name, TradeConfig tradeConfig, EndofDayConfig eodc, IntradayConfig indc) {
        this();
        this.name = name;
        this.tradeConfig = tradeConfig;
        this.intradayConfig = indc;
        this.endofdayConfig = eodc;
    }

    public String getName() {
        return name;
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
