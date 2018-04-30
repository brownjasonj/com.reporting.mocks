package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.process.CompleteProcess;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.RiskRunType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TradePVController {

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

        if (CompleteProcess.startProcess(pricingGroupName) != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
