package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.configuration.ConfigurationManager;
import com.reporting.mocks.configuration.PricingGroupConfig;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigController {
    @RequestMapping(method = { RequestMethod.POST }, value = { "/setConfig" }, produces = "application/json")
    public Config getConfiguration(@RequestBody Config config) {
        ConfigurationManager.getConfigurationManager().setConfig(config);
        return ConfigurationManager.getConfigurationManager().getConfig();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getConfig" }, produces = "application/json")
    public Config getConfiguration() {
        return ConfigurationManager.getConfigurationManager().getConfig();
    }


    @RequestMapping(method = { RequestMethod.POST }, value = { "/setPricingGroupConfig/{pricingGroup}" }, produces = "application/json")
    public PricingGroupConfig getConfiguration(@PathVariable String pricingGroupName, @RequestBody PricingGroupConfig pricingGroupConfig) {
        return ConfigurationManager.getConfigurationManager().getConfig().addPricingGroup(pricingGroupConfig);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getPricingGroupConfig" }, produces = "application/json")
    public PricingGroupConfig getConfiguration(@RequestParam("name") String name) {
        return ConfigurationManager.getConfigurationManager().getPriceingGroupConfig(name);
    }
}
