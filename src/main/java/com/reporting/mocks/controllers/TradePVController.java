package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.CompleteProcess;
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

        this.config = new Config();

        PricingGroupConfig pgc = new PricingGroupConfig("FXDesk", tradeConfig, eodc, indc);
        this.config.addPricingGroup(pgc);

        CompleteProcess.addProcess(new CompleteProcess(pgc));
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


    @RequestMapping(method = { RequestMethod.POST }, value = { "/setConfig/{pricingGroup}" }, produces = "application/json")
    public PricingGroupConfig getConfiguration(@PathVariable String pricingGroupName, @RequestBody PricingGroupConfig pricingGroupConfig) {
        return this.config.addPricingGroup(pricingGroupConfig);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getPricingGroupConfig" }, produces = "application/json")
    public PricingGroupConfig getConfiguration(@RequestParam("name") String name) {
        return this.config.getPricingGroup(name);
    }


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

    @RequestMapping(method = { RequestMethod.GET }, value = { "/getTradePopulation/{pricingGroupName}" }, produces = "application/json")
    public Collection<TradePopulation> tradePopulations(@PathVariable String pricingGroupName) {

        //return TradeStore.getStore().getAllTradePopulation();
        return null;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/riskrun/{riskRunType}" }, produces = "application/json")
    public RiskRunResult solver2(@PathVariable RiskRunType riskRunType, @RequestParam("tradePopulationId") UUID tradePopulationId) {
//        List<RiskType> risks = new ArrayList<>();
//        risks.add(RiskType.PV);
//        risks.add(RiskType.DELTA);
//        TradePopulation tradePop = TradeStore.getStore().getTradePopulation(tradePopulationId);
//        MarketEnv marketEnv = new MarketEnv();
//
//        // RiskRunType type, Collection<Trade> trades, List<RiskType> risks
//        RiskRunRequest riskRun = new RiskRunRequest(riskRunType, marketEnv, tradePop, risks);
//        return RiskRunGenerator.generate(riskRun);
        return null;
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/startprocess/{pricingGroupName}" }, produces = "application/json")
    public Boolean runCompleteProcess(@PathVariable String pricingGroupName) {
//        CompleteProcess completeProcess = new CompleteProcess(this.pricingGroups);
//        new Thread(completeProcess).start();

        CompleteProcess proc = CompleteProcess.getProcess(pricingGroupName);
        if (proc != null) {
            new Thread(proc).start();
            return true;
        }
        else {
            return false;
        }
    }
}
