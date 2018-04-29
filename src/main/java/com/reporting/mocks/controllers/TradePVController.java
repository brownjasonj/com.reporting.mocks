package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.Config;
import com.reporting.mocks.configuration.EndofDayConfig;
import com.reporting.mocks.configuration.IntradayConfig;
import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TradePVController {
    public Config config;

    public TradePVController() {
        ArrayList<String> books = new ArrayList<>(Arrays.asList("Book1", "Book2", "Book3"));
        ArrayList<String> currency = new ArrayList<>(Arrays.asList("EUR", "USD", "CHF", "GBP", "JPY", "MXN", "RBL", "AUD"));
        TradeConfig tradeConfig = new TradeConfig(books, currency);

        ArrayList<RiskType> eodr = new ArrayList<>(Arrays.asList(RiskType.PV, RiskType.DELTA));
        EndofDayConfig eodc = new EndofDayConfig(eodr);

        ArrayList<IntradayRiskType> indr = new ArrayList<>(Arrays.asList(new IntradayRiskType(RiskType.PV, 0), new IntradayRiskType(RiskType.PV, 0)));
        IntradayConfig indc = new IntradayConfig(indr);

        this.config = new Config(tradeConfig, eodc, indc);
    }

    @RequestMapping(method = { RequestMethod.POST }, value = { "/setConfig" }, produces = "application/json")
    public Config getConfiguration(@RequestBody Config config) {
        this.config = config;
        return this.config;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getConfig" }, produces = "application/json")
    public Config getConfiguration() {
        return this.config;
    }


    @RequestMapping(method = { RequestMethod.GET }, value = { "/trades/{count}" }, produces = "application/json")
    public TradePopulation tradepv(@PathVariable int count) {
        TradeStore tradeStore = TradeStore.getStore();
        this.config.getTradeConfig().setStartingTradeCount(count);
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
