package com.reporting.mocks.controllers;

import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.persistence.CalculationContextStore;
import com.reporting.mocks.persistence.CalculationContextStoreFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class MarketEnvController {

    @RequestMapping(method = { RequestMethod.GET }, value = { "/calculationcontext/pricinggroup" }, produces = "application/json")
    public Collection<CalculationContext> getCalculationContext(@RequestParam("name") String pricingGroupName) {
//        CalculationContextStore store = CalculationContextStoreFactory.get(pricingGroupName);
//        if (store != null) {
//            return store.getAll();
//        }
        return null;
    }

}
