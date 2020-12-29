package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.interfaces.persistence.ITradePopulation;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.trade.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
public class TradeController {
    @Autowired
    IPersistenceStoreFactory<ITradeStore> tradeStoreFactory;

    @Autowired
    Configurations configurations;

    @Autowired
    public TradeController(IPersistenceStoreFactory<ITradeStore> tradeStoreFactory) {
        this.tradeStoreFactory = tradeStoreFactory;
    }

    @GetMapping("/tradepopulations/{pricingGroupName}")
    public List<TradePopulationId> tradePopulations(@PathVariable String pricingGroupName) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            ITradeStore store = this.tradeStoreFactory.get(pricingGroupConfig.getPricingGroupId());
            if (store != null) {
                return store.getTradePopulationsIds();
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("/tradepopulation/{pricingGroupName}/{id}")
    public Collection<Trade> tradePopulation(@PathVariable String pricingGroupName, @PathVariable UUID id) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            ITradeStore store = this.tradeStoreFactory.get(pricingGroupConfig.getPricingGroupId());
            if (store != null) {
                ITradePopulation tradePopulation = store.getTradePopulationById(new TradePopulationId(pricingGroupName, id));
                if (tradePopulation != null)
                    return tradePopulation.getTrades();
            }
        }
        return new ArrayList<>();
    }
}
