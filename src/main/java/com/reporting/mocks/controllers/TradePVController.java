package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TradePVController {
    @RequestMapping(method = { RequestMethod.GET }, value = { "/trades/{count}" }, produces = "application/json")
    public TradePopulation tradepv(@PathVariable int count) {
        ArrayList<String> books = new ArrayList<String>(Arrays.asList("Book1", "Book2", "Book3"));
        ArrayList<String> currency = new ArrayList<String>(Arrays.asList("EUR", "USD", "CHF", "GBP", "JPY", "MXN", "RBL", "AUD"));
        Config config = new Config(count, books, currency);
        TradeStore tradeStore = TradeStore.getStore();
        for(Trade t : TradeGenerator.generator(config)) {
            tradeStore.putTrade(t);
        }
        return tradeStore.getTradePopulation();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/tradepopulations" }, produces = "application/json")
    public Collection<TradePopulation> tradePopulations() {
        return TradeStore.getStore().getAllTradePopulation();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/riskrun/{riskRunType}" }, produces = "application/json")
    public RiskRunResult solver2(@PathVariable RiskRunType riskRunType, @RequestParam("tradePopulationId") UUID tradePopulationId) {
        List<RiskType> risks = new ArrayList<>();
        risks.add(RiskType.PV);
        risks.add(RiskType.DELTA);
        TradePopulation tradePop = TradeStore.getStore().getTradePopulation(tradePopulationId);
        MarketEnv marketEnv = new MarketEnv();

        // RiskRunType type, Collection<Trade> trades, List<RiskType> risks
        RiskRunRequest riskRun = new RiskRunRequest(riskRunType, marketEnv, tradePop, risks);
        return RiskRunGenerator.generate(riskRun);
    }
}
