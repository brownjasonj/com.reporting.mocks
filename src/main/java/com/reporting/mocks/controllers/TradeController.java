package com.reporting.mocks.controllers;

import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.persistence.TradeStoreFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
public class TradeController {
    @RequestMapping(method = { RequestMethod.GET }, value = { "/TradePopulations/{pricingGroupName}" }, produces = "application/json")
    public List<TradePopulationId> tradePopulations(@PathVariable String pricingGroupName) {
        TradeStore store = TradeStoreFactory.get().get(pricingGroupName);
        if (store != null) {
            return store.getTradePopulationsIds();
        } else {
            return null;
        }
    }
    @RequestMapping(method = { RequestMethod.GET }, value = { "/TradePopulation/{pricingGroupName}" }, produces = "application/json")
    public Collection<Trade> tradePopulation(@PathVariable String pricingGroupName, @RequestParam("id") UUID id) {
        TradeStore store = TradeStoreFactory.get().get(pricingGroupName);
        if (store != null) {
            TradePopulation tradePopulation = store.getTradePopulation(id);
            if (tradePopulation != null)
                return tradePopulation.getAllTrades();
        }
        return null;
    }
}
