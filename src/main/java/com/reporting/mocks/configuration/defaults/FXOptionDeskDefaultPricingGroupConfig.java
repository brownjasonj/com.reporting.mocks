package com.reporting.mocks.configuration.defaults;

import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.CurrencyPair;
import com.reporting.mocks.model.underlying.OtcUnderlying;
import com.reporting.mocks.model.underlying.SecurityStatic;

import java.util.ArrayList;
import java.util.List;

public class FXOptionDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    public FXOptionDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        List<OtcUnderlying> otcUnderlying = new ArrayList<>();
        List<TradeType> otcTradeTypes = new ArrayList<>();
        List<SecurityStatic> securityStatic = new ArrayList<>();

        // - URN: book:<department name>:<desk name>:<book name>
        //   e.g., book:fxdesk:fxspots:bookname

        books.add("bank:fxdesk:fxoptions:LATAM");
        books.add("bank:fxdesk:fxoptions:EMEA");
        books.add("bank:fxdesk:fxotpions:APAC");


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
        otcTradeTypes.add(TradeType.VanillaOption);
        otcTradeTypes.add(TradeType.BarrierOption);

        this.tradeConfig = new TradeConfig(books, otcUnderlying, otcTradeTypes, securityStatic);

        ArrayList<RiskType> eodr = new ArrayList<>();
        eodr.add(RiskType.PV);
        eodr.add(RiskType.DELTA);
        eodr.add(RiskType.VEGA);
        this.endofdayConfig = new EndofDayConfig(eodr, 5 * 60 * 1000);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        indr.add(new IntradayRiskType(RiskType.VEGA, 3));
        this.intradayConfig = new IntradayConfig(indr);


        this.pricingGroupId = new PricingGroup("fxoptiondesk");
    }
}
