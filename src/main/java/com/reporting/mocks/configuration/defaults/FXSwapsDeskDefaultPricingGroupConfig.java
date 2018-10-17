package com.reporting.mocks.configuration.defaults;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FXSwapsDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    protected final int startingTradeCount = 100;
    protected final int newTradeStart = 0;
    protected final int newTradePeriodicity = 1000;        // number of milliseconds between new tcnTrades (default: 10s)

    protected final int modifiedTradeStart = 60 * 1000;
    protected final int modifiedTradePeriodicity = 60 * 1000;    // number of milliseconds between trade modifications (default: 30s)

    protected final int deleteTadeStart = 120 * 1000;
    protected final int deleteTradePeriodicity = 120 * 1000;

    public FXSwapsDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        UnderlyingConfig underlyings = new UnderlyingConfig();
        List<TradeType> otcTradeTypes = new ArrayList<>();

        this.pricingGroupId = new PricingGroup("fxswapdesk");
        // - URN: book:<department pricingGroup>:<desk pricingGroup>:<book pricingGroup>
        //   e.g., book:fxdesk:fxspots:bookname
        books.add("bank:fxdesk:fxswaps:Hedge");
        books.add("bank:fxdesk:fxswaps:BankBook");
        books.add("bank:fxdesk:fxswaps:Americas");
        books.add("bank:fxdesk:fxswaps:EMEA");

        underlyings.addSet("EUR", Arrays.asList("USD", "CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));
        underlyings.addSet("USD", Arrays.asList("CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));

        otcTradeTypes.add(TradeType.Spot);
        otcTradeTypes.add(TradeType.Forward);
        otcTradeTypes.add(TradeType.Swap);
        otcTradeTypes.add(TradeType.VanillaOption);

        this.tradeConfig = new TradeConfig(books, underlyings, otcTradeTypes);
        this.tradeConfig.setStartingTradeCount(startingTradeCount);
        this.tradeConfig.setNewTradeStart(newTradeStart);
        this.tradeConfig.setNewTradePeriodicity(newTradePeriodicity);
        this.tradeConfig.setModifiedTradeStart(modifiedTradeStart);
        this.tradeConfig.setModifiedTradePeriodicity(modifiedTradePeriodicity);
        this.tradeConfig.setDeleteTadeStart(deleteTadeStart);
        this.tradeConfig.setDeleteTradePeriodicity(deleteTradePeriodicity);

        ArrayList<RiskType> eodr = new ArrayList<>();
        eodr.add(RiskType.PV);
        eodr.add(RiskType.DELTA);
        eodr.add(RiskType.GAMMA);
        this.endofdayConfig = new EndofDayConfig(eodr, 5 * 60 * 1000);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        indr.add(new IntradayRiskType(RiskType.GAMMA, 3));
        this.intradayConfig = new IntradayConfig(indr);
    }}
