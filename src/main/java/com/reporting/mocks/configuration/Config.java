package com.reporting.mocks.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Config {
    protected Map<String, PricingGroupConfig> pricingGroups;

    public Config() {
        this.pricingGroups = new HashMap<>();
    }

    public Collection<PricingGroupConfig> getPricingGroups() {
        return pricingGroups.values();
    }

    public PricingGroupConfig addPricingGroup(PricingGroupConfig pricingGroup) {
        return this.pricingGroups.put(pricingGroup.getPricingGroupId().getName(), pricingGroup);
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
