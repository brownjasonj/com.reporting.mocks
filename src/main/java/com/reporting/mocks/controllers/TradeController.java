package com.reporting.mocks.controllers;

import com.reporting.mocks.interfaces.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.interfaces.persistence.ITradePopulation;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
public class TradeController {
    @Autowired
    IPersistenceStoreFactory<ITradeStore> tradeStoreFactory;

    @Autowired
    public TradeController(IPersistenceStoreFactory<ITradeStore> tradeStoreFactory) {
        this.tradeStoreFactory = tradeStoreFactory;
    }

    @GetMapping("/tradepopulations/{pricingGroupName}")
    public List<TradePopulationId> tradePopulations(@PathVariable String pricingGroupName) {
        ITradeStore store = this.tradeStoreFactory.get(new PricingGroup(pricingGroupName));
        if (store != null) {
            return store.getTradePopulationsIds();
        } else {
            return null;
        }
    }

    @GetMapping("/tradepopulation/{pricingGroupName}/{id}")
    public Collection<Trade> tradePopulation(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        ITradeStore store = this.tradeStoreFactory.get(new PricingGroup(pricingGroupName));
        if (store != null) {
            ITradePopulation tradePopulation = store.getTradePopulationById(new TradePopulationId(pricingGroupName,id));
            if (tradePopulation != null)
                return tradePopulation.getTrades();
        }
        return null;
    }
}
