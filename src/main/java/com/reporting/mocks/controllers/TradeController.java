package com.reporting.mocks.controllers;

import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.underlying.SecurityStatic;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.persistence.TradeStoreFactory;
import com.reporting.mocks.process.CompleteProcess;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class TradeController {
    @RequestMapping(method = { RequestMethod.GET }, value = { "/TradePopulation/{pricingGroupName}" }, produces = "application/json")
    public TradePopulation tradePopulations(@PathVariable String pricingGroupName, @RequestParam("id") UUID id) {
        TradeStore store = TradeStoreFactory.get().get(pricingGroupName);
        if (store != null) {
            return store.get(id);
        } else {
            return null;
        }
    }
}
