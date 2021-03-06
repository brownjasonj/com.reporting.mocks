package com.reporting.mocks.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.reporting.mocks.configuration.defaults.FXOptionDeskDefaultPricingGroupConfig;
import com.reporting.mocks.configuration.defaults.FXSpotDeskDefaultPricingGroupConfig;
import com.reporting.mocks.configuration.defaults.FXForwardsDeskDefaultPricingGroupConfig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope
public class Configurations {
    protected Map<String, PricingGroupConfig> pricingGroups;

    public Configurations() {
        this.pricingGroups = new HashMap<>();

        this.addPricingGroup(new FXForwardsDeskDefaultPricingGroupConfig());
        this.addPricingGroup(new FXOptionDeskDefaultPricingGroupConfig());
        this.addPricingGroup(new FXSpotDeskDefaultPricingGroupConfig());
    }

    public Collection<PricingGroupConfig> getPricingGroups() {
        return pricingGroups.values();
    }

    public PricingGroupConfig addPricingGroup(PricingGroupConfig pricingGroup) {
        this.pricingGroups.put(pricingGroup.getPricingGroupId().getName(), pricingGroup);
        return this.getPricingGroup(pricingGroup.getPricingGroupId().getName());
    }

    public PricingGroupConfig getPricingGroup(String name) {
        if (this.pricingGroups.containsKey(name)) {
            return this.pricingGroups.get(name);
        }
        else {
            return null;
        }
    }
}
