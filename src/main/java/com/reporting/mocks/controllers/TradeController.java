package com.reporting.mocks.controllers;

import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.underlying.SecurityStatic;
import com.reporting.mocks.process.CompleteProcess;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class TradeController {
    @RequestMapping(method = { RequestMethod.GET }, value = { "/trades/{pricinggroup}/{count}" }, produces = "application/json")
    public TradePopulation tradepv(@PathVariable String pricingGroupName, @PathVariable int count) {
//        TradeStore tradeStore = TradeStore.getStore();
//        this.pricingGroups.getTradeConfig().setStartingTradeCount(count);
//        for(Trade t : TradeGenerator.generator(pricingGroups)) {
//            tradeStore.putTrade(t);
//        }
//        return tradeStore.getTradePopulation();
        return null;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getSecurityStatic/{pricingGroupName}" }, produces = "application/json")
    public Collection<SecurityStatic> getSecurityStatic(@PathVariable String pricingGroupName) {
        return null;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getTradePopulations/{pricingGroupName}" }, produces = "application/json")
    public Collection<TradePopulation> tradePopulations(@PathVariable String pricingGroupName) {
        CompleteProcess proc = CompleteProcess.getProcess(pricingGroupName);
        if (proc != null) {
            return proc.getTradePopulations();
        }
        else {
            return null;
        }
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getTradePopulations/{pricingGroupName}/{populationId}" }, produces = "application/json")
    public TradePopulation tradePopulations(@PathVariable String pricingGroupName, @PathVariable UUID populationId) {
        CompleteProcess proc = CompleteProcess.getProcess(pricingGroupName);
        if (proc != null) {
            return proc.getTradePopulation(populationId);
        }
        else {
            return null;
        }
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getTrade/{pricingGroupName}/{populationId}/{tradeId}" }, produces = "application/json")
    public Trade tradePopulations(@PathVariable String pricingGroupName, @PathVariable UUID populationId, @PathVariable UUID tradeId) {
        CompleteProcess proc = CompleteProcess.getProcess(pricingGroupName);
        if (proc != null) {
            TradePopulation tradePopulation = proc.getTradePopulation(populationId);
            if (tradePopulation != null)
                return tradePopulation.getTrade(tradeId);
        }
        return null;
    }

}
