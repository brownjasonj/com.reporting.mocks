package com.reporting.mocks.controllers;

import com.reporting.mocks.process.CompleteProcess;
import org.springframework.web.bind.annotation.*;

@RestController
public class TradePVController {

//    @RequestMapping(method = { RequestMethod.GET }, value = { "/riskrun/{riskRunType}" }, produces = "application/json")
//    public RiskRunResult solver2(@PathVariable RiskRunType riskRunType, @RequestParam("tradePopulationId") UUID tradePopulationId) {
//        List<RiskType> risks = new ArrayList<>();
//        risks.add(RiskType.PV);
//        risks.add(RiskType.DELTA);
//        TradePopulation tradePop = TradeStore.getStore().getTradePopulation(tradePopulationId);
//        MarketEnv marketEnv = new MarketEnv();
//
//        // RiskRunType type, Collection<Trade> trades, List<RiskType> risks
//        RiskRunRequest riskRun = new RiskRunRequest(riskRunType, marketEnv, tradePop, risks);
//        return RiskRunGenerator.generate(riskRun);
//        return null;
//    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/startprocess/{pricingGroupName}" }, produces = "application/json")
    public Boolean startCompleteProcess(@PathVariable String pricingGroupName) {
        CompleteProcess completeProcess = CompleteProcess.getProcess(pricingGroupName);

        if (completeProcess != null) {
            completeProcess.start();
            return true;
        }
        else {
            return false;
        }
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/stopprocess/{pricingGroupName}" }, produces = "application/json")
    public Boolean stopCompleteProcess(@PathVariable String pricingGroupName) {
        CompleteProcess completeProcess = CompleteProcess.getProcess(pricingGroupName);
        if (completeProcess != null) {
            completeProcess.stop();
            return true;
        }
        else {
            return false;
        }
    }
}
