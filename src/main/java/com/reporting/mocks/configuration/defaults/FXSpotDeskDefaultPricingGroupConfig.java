package com.reporting.mocks.configuration.defaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;

public class FXSpotDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    protected final int startingTradeCount = 170000;
    protected final int newTradeStart = 0;
    protected final int newTradePeriodicity = 100;        // number of milliseconds between new tcnTrades (default: 10s)

    protected final int modifiedTradeStart = 1000;
    protected final int modifiedTradePeriodicity = 1000;    // number of milliseconds between trade modifications (default: 30s)
    protected final boolean modifyPostReverse = false;           // a modify even does not create two values
    protected final boolean riskOnDelete = false;


    protected final int deleteTadeStart = 0;
    protected final int deleteTradePeriodicity = 1000;

    protected final int eodMarketPeridocity = 20 * 60 * 60 * 1000;
    protected final int intradayMarketPeriodicity = 15 * 60 * 1000;

    public FXSpotDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        UnderlyingConfig underlyings = new UnderlyingConfig();
        Map<TradeType, List<RiskType>> tradeTypeAndRisks = new HashMap<>();


        // - URN: book:<department pricingGroup>:<desk pricingGroup>:<book pricingGroup>
        //   e.g., book:fxdesk:fxspots:bookname
        books.add("bank:macro:apac:fx");
        books.add("bank:macro:apac:structeqty");
        books.add("bank:macro:apac:eqtyother");
        books.add("bank:macro:em:emea");
        books.add("bank:macro:em:latam");

        this.pricingGroupId = new PricingGroup("fxspotdesk", books);


        underlyings.addSet("EUR", Arrays.asList("USD", "CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));
        underlyings.addSet("USD", Arrays.asList("CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));

        tradeTypeAndRisks.put(TradeType.Payment, Arrays.asList(RiskType.PV));
        tradeTypeAndRisks.put(TradeType.Spot, Arrays.asList(RiskType.PV, RiskType.DELTA));
        tradeTypeAndRisks.put(TradeType.Forward, Arrays.asList(RiskType.PV, RiskType.DELTA));
        tradeTypeAndRisks.put(TradeType.Swap, Arrays.asList(RiskType.PV, RiskType.DELTA));


        this.tradeConfig = new TradeConfig(books, underlyings, tradeTypeAndRisks);
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
        this.endofdayConfig = new EndofDayConfig(eodr, eodMarketPeridocity);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        this.intradayConfig = new IntradayConfig(indr, intradayMarketPeriodicity, modifyPostReverse, riskOnDelete);


    }
}
