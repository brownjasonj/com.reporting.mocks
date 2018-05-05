package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.CurrencyPair;
import com.reporting.mocks.model.underlying.OtcUnderlying;
import com.reporting.mocks.model.underlying.SecurityStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationManager {
    protected static ConfigurationManager configManager = null;

    public static ConfigurationManager getConfigurationManager() {
        if (ConfigurationManager.configManager == null) {
            ConfigurationManager.configManager = new ConfigurationManager();
            Config config = new Config();
            ConfigurationManager.configManager.setConfig(config);

            ArrayList<String> books = new ArrayList<>(Arrays.asList("Book1", "Book2", "Book3"));
            List<OtcUnderlying> otcUnderlying = new ArrayList<>();
            List<TradeType> otcTradeTypes = new ArrayList<>();
            List<SecurityStatic > securityStatic = new ArrayList<>();

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


            TradeConfig tradeConfig = new TradeConfig(books, otcUnderlying, otcTradeTypes, securityStatic);

            ArrayList<RiskType> eodr = new ArrayList<>(Arrays.asList(RiskType.PV, RiskType.DELTA));
            EndofDayConfig eodc = new EndofDayConfig(eodr, 10 * 1000);

            ArrayList<IntradayRiskType> indr = new ArrayList<>(Arrays.asList(new IntradayRiskType(RiskType.PV, 0), new IntradayRiskType(RiskType.DELTA, 0)));
            IntradayConfig indc = new IntradayConfig(indr);


            PricingGroupConfig pgc = new PricingGroupConfig("FXDesk", tradeConfig, eodc, indc);
            config.addPricingGroup(pgc);

        }
        return ConfigurationManager.configManager;
    }


    protected Config config;

    protected ConfigurationManager() {
    }

    public PricingGroupConfig getPriceingGroupConfig(String name) {
        return config.getPricingGroup(name);
    }

    public Config getConfig() {
        return this.config;
    }

    public Config setConfig(Config config) {
        this.config = config;
        return this.config;
    }
}

