package com.reporting.mocks.configuration;

import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigurationManager {
    protected static ConfigurationManager configManager = null;

    public static ConfigurationManager getConfigurationManager() {
        if (ConfigurationManager.configManager == null) {
            ConfigurationManager.configManager = new ConfigurationManager();
            Config config = new Config();
            ConfigurationManager.configManager.setConfig(config);

            ArrayList<String> books = new ArrayList<>(Arrays.asList("Book1", "Book2", "Book3"));
            ArrayList<String> currency = new ArrayList<>(Arrays.asList("EUR", "USD", "CHF", "GBP", "JPY", "MXN", "RBL", "AUD"));
            TradeConfig tradeConfig = new TradeConfig(books, currency);

            ArrayList<RiskType> eodr = new ArrayList<>(Arrays.asList(RiskType.PV, RiskType.DELTA));
            EndofDayConfig eodc = new EndofDayConfig(eodr, 60 * 1000);

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

