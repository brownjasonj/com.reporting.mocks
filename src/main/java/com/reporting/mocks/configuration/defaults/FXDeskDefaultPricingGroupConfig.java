package com.reporting.mocks.configuration.defaults;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.CurrencyPair;
import com.reporting.mocks.model.underlying.OtcUnderlying;
import com.reporting.mocks.model.underlying.SecurityStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FXDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    public FXDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        List<OtcUnderlying> otcUnderlying = new ArrayList<>();
        List<TradeType> otcTradeTypes = new ArrayList<>();
        List<SecurityStatic> securityStatic = new ArrayList<>();

        // - URN: book:<department name>:<desk name>:<book name>
        //   e.g., book:fxdesk:fxspots:bookname

        books.add("book:fxdesk:fxspots:Book1");
        books.add("book:fxdesk:fxspots:Book2");
        books.add("book:fxdesk:fxspots:Book3");


        otcUnderlying.add(new CurrencyPair("EUR", "USD"));
        otcUnderlying.add(new CurrencyPair("EUR", "CHF"));
        otcUnderlying.add(new CurrencyPair("EUR", "GBP"));
        otcUnderlying.add(new CurrencyPair("EUR", "MXN"));
        otcUnderlying.add(new CurrencyPair("EUR", "JPY"));
        otcUnderlying.add(new CurrencyPair("EUR", "AUD"));
        otcUnderlying.add(new CurrencyPair("EUR", "RBL"));
        otcUnderlying.add(new CurrencyPair("USD", "CHF"));
        otcUnderlying.add(new CurrencyPair("USD", "GBP"));
        otcUnderlying.add(new CurrencyPair("USD", "MXN"));
        otcUnderlying.add(new CurrencyPair("USD", "JPY"));
        otcUnderlying.add(new CurrencyPair("USD", "AUD"));
        otcUnderlying.add(new CurrencyPair("USD", "RBL"));

        otcTradeTypes.add(TradeType.Spot);
        otcTradeTypes.add(TradeType.Forward);
        otcTradeTypes.add(TradeType.Swap);

        this.tradeConfig = new TradeConfig(books, otcUnderlying, otcTradeTypes, securityStatic);

        ArrayList<RiskType> eodr = new ArrayList<>();
        eodr.add(RiskType.PV);
        eodr.add(RiskType.DELTA);
        eodr.add(RiskType.VEGA);
        this.endofdayConfig = new EndofDayConfig(eodr, 10 * 60 * 1000);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        indr.add(new IntradayRiskType(RiskType.VEGA, 3));
        this.intradayConfig = new IntradayConfig(indr);


        this.pricingGroupId = new PricingGroup("fxdesk");
    }
}
