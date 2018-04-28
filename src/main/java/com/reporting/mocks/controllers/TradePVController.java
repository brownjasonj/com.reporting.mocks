package com.reporting.mocks.controllers;

import com.reporting.mocks.generators.RiskRunGenerator;
import com.reporting.mocks.generators.TradeGenerator;
import com.reporting.mocks.model.RiskRun;
import com.reporting.mocks.model.RiskRunType;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class TradePVController {
    @RequestMapping(method = { RequestMethod.GET }, value = { "/trades/{count}" }, produces = "application/json")
    public Collection<Trade> tradepv(@PathVariable int count) {
        TradeStore tradeStore = TradeStore.getStore();
        for(Trade t : TradeGenerator.generator(count)) {
            tradeStore.putTrade(t);
        }
        return tradeStore.getAll();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "/riskrun/{riskRunType}" }, produces = "application/json")
    public List<Risk> solver2(@PathVariable RiskRunType riskRunType) {
        Collection<Trade> trades = TradeStore.getStore().getAll();
        List<RiskType> risks = new ArrayList<>();
        risks.add(RiskType.PV);
        risks.add(RiskType.DELTA);

        // RiskRunType type, Collection<Trade> trades, List<RiskType> risks
        RiskRun riskRun = new RiskRun(riskRunType, trades, risks);
        return RiskRunGenerator.generate(riskRun);
    }
}
