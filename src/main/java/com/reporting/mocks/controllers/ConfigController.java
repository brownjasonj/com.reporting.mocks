package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.configuration.ConfigurationManager;
import com.reporting.mocks.configuration.PricingGroupConfig;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@RestController
public class ConfigController {
    @GetMapping("/getpricinggroups")
    public Config getPricingGroups(){
        return ConfigurationManager.getConfigurationManager().getConfig();
    }

    @GetMapping("/getpricinggroup/{name}")
    public PricingGroupConfig getPricingGroupConfig(@PathVariable String name) {
        return ConfigurationManager.getConfigurationManager().getPriceingGroupConfig(name);
    }

    @PostMapping("/setpricinggroup/{name}")
    public PricingGroupConfig setPricingGroupConfig(@PathVariable String name, @RequestBody PricingGroupConfig config) {
        return ConfigurationManager.getConfigurationManager().getConfig().addPricingGroup(config);
    }
}
