package com.reporting.mocks.configuration.defaults;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.SecurityStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FXOptionDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    public FXOptionDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        UnderlyingConfig underlyings = new UnderlyingConfig();
        List<TradeType> otcTradeTypes = new ArrayList<>();

        // - URN: book:<department pricingGroup>:<desk pricingGroup>:<book pricingGroup>
        //   e.g., book:fxdesk:fxspots:bookname

        books.add("bank:fxdesk:fxoptions:LATAM");
        books.add("bank:fxdesk:fxoptions:EMEA");
        books.add("bank:fxdesk:fxotpions:APAC");

        underlyings.addSet("EUR", Arrays.asList("USD", "CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));
        underlyings.addSet("USD", Arrays.asList("CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));

        otcTradeTypes.add(TradeType.Spot);
        otcTradeTypes.add(TradeType.Forward);
        otcTradeTypes.add(TradeType.Swap);
        otcTradeTypes.add(TradeType.VanillaOption);
        otcTradeTypes.add(TradeType.BarrierOption);

        this.tradeConfig = new TradeConfig(books, underlyings, otcTradeTypes);

        ArrayList<RiskType> eodr = new ArrayList<>();
        eodr.add(RiskType.PV);
        eodr.add(RiskType.DELTA);
        eodr.add(RiskType.GAMMA);
        this.endofdayConfig = new EndofDayConfig(eodr, 5 * 60 * 1000);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        indr.add(new IntradayRiskType(RiskType.VEGA, 3));
        this.intradayConfig = new IntradayConfig(indr);


        this.pricingGroupId = new PricingGroup("fxoptiondesk");
    }
}
