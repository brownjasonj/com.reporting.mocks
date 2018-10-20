package com.reporting.mocks.configuration;

import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.RiskType;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PricingGroupConfig {
    protected PricingGroup pricingGroupId;
    protected TradeConfig tradeConfig;
    protected IntradayConfig intradayConfig;
    protected EndofDayConfig endofdayConfig;

    protected int marketPeriodicity = 30 * 1000;   // milliseconds between change in market data.

    public PricingGroupConfig() {
    }

    public PricingGroupConfig(PricingGroup pricingGroupId,
                              TradeConfig tradeConfig,
                              EndofDayConfig eodc,
                              IntradayConfig indc) {
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

    public List<RiskType> findAllRiskTypes() {
        HashSet<RiskType> risks = new HashSet<>(endofdayConfig.getRisks());
        risks.addAll(intradayConfig.getRisks().stream().map(ir -> ir.getRiskType()).collect(Collectors.toList()));
        return risks.stream().collect(Collectors.toList());
    }

    public int getMarketPeriodicity() {
        return marketPeriodicity;
    }
}
