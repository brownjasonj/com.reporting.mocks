package com.reporting.mocks.controllers;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.persistence.*;
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
    CalculationContextController(IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory, IPersistenceStoreFactory<IMarketStore> marketStoreFactory) {
        this.calculationContextStoreFactory = calculationContextStoreFactory;
        this.marketStoreFactory = marketStoreFactory;
    }

    @GetMapping("/calculationcontext/{pricingGroupName}")
    public Collection<CalculationContext> getCalculationContexts(@PathVariable String pricingGroupName) {
        ICalculationContextStore store = this.calculationContextStoreFactory.get(new PricingGroup(pricingGroupName));
        if (store != null) {
            return store.getAll();
        }
        return null;
    }


    @GetMapping("/calculationcontext/{pricingGroupName}/{id}")
    public Collection<CalculationContext> getCalculationContext(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        ICalculationContextStore store = this.calculationContextStoreFactory.get(new PricingGroup(pricingGroupName));
        if (store != null) {
            if (id == null)
                return store.getAll();
            else
                return new ArrayList<>(Arrays.asList(store.get(id)));
        }
        return null;
    }

    @GetMapping("/calculationcontext/market/{pricingGroupName}/{id}")
    public MarketEnv getMarketEnvironment(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        IMarketStore store = this.marketStoreFactory.get(new PricingGroup(pricingGroupName));
        if (store != null) {
            return store.get(id);
        }
        return null;
    }
}
