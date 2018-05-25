package com.reporting.mocks.controllers;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.persistence.CalculationContextStore;
import com.reporting.mocks.persistence.CalculationContextStoreFactory;
import com.reporting.mocks.persistence.MarketStore;
import com.reporting.mocks.persistence.MarketStoreFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@RestController
public class MarketEnvController {

//    @RequestMapping(method = { RequestMethod.GET }, value = { "/CalculationContextId/{pricingGroupName}" }, produces = "application/json")
//    public Collection<CalculationContext> getCalculationContexts(@PathVariable String pricingGroupName) {
//        CalculationContextStore store = CalculationContextStoreFactory.get(pricingGroupName);
//        if (store != null) {
//            return store.getAll();
//        }
//        return null;
//    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/CalculationContext/{pricingGroupName}" }, produces = "application/json")
    public Collection<CalculationContext> getCalculationContext(@PathVariable String pricingGroupName, @RequestParam("id") UUID id) {
        CalculationContextStore store = CalculationContextStoreFactory.get(pricingGroupName);
        if (store != null) {
            if (id == null)
                return store.getAll();
            else
                return new ArrayList<>(Arrays.asList(store.get(id)));
        }
        return null;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/MarketEnvironment/{pricingGroupName}" }, produces = "application/json")
    public MarketEnv getMarketEnvironment(@PathVariable String pricingGroupName, @RequestParam("id") UUID id) {
        MarketStore store = MarketStoreFactory.get(pricingGroupName);
        if (store != null) {
            return store.get(id);
        }
        return null;
    }
}
