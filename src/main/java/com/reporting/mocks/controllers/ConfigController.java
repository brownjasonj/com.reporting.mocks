package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigController {
    @Autowired
    Configurations configurations;

    @Autowired
    public ConfigController(Configurations configurations) {
        this.configurations = configurations;
    }

    @GetMapping("/getpricinggroups")
    public Configurations getPricingGroups(){
        return this.configurations;
    }

    @GetMapping("/getpricinggroup/{name}")
    public PricingGroupConfig getPricingGroupConfig(@PathVariable String name) {
        return this.configurations.getPricingGroup(name);
    }

    @PostMapping("/setpricinggroup/{name}")
    public PricingGroupConfig setPricingGroupConfig(@PathVariable String name, @RequestBody PricingGroupConfig config) {
        return this.configurations.addPricingGroup(config);
    }
}
