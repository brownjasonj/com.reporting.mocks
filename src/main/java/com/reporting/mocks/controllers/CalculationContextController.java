package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.IMarketStore;
import com.reporting.mocks.interfaces.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@RestController
public class CalculationContextController {
    @Autowired
    IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory;

    @Autowired
    IPersistenceStoreFactory<IMarketStore> marketStoreFactory;

    @Autowired
    Configurations configurations;

    @Autowired
    CalculationContextController(IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory, IPersistenceStoreFactory<IMarketStore> marketStoreFactory) {
        this.calculationContextStoreFactory = calculationContextStoreFactory;
        this.marketStoreFactory = marketStoreFactory;
    }

    @GetMapping("/calculationcontext/{pricingGroupName}")
    public Collection<CalculationContext> getCalculationContexts(@PathVariable String pricingGroupName) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            ICalculationContextStore store = this.calculationContextStoreFactory.get(pricingGroupConfig.getPricingGroupId());
            if (store != null) {
                return store.getAll();
            }
        }
        return new ArrayList<>();
    }


    @GetMapping("/calculationcontext/{pricingGroupName}/{id}")
    public Collection<CalculationContext> getCalculationContext(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            ICalculationContextStore store = this.calculationContextStoreFactory.get(pricingGroupConfig.getPricingGroupId());
            if (store != null) {
                if (id == null)
                    return store.getAll();
                else
                    return new ArrayList<>(Arrays.asList(store.get(id)));
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("/calculationcontext/market/{pricingGroupName}/{id}")
    public MarketEnv getMarketEnvironment(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            IMarketStore store = this.marketStoreFactory.get(pricingGroupConfig.getPricingGroupId());
            if (store != null) {
                return store.get(id);
            }
        }
        return null;
    }
}
