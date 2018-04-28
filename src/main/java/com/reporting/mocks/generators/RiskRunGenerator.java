package com.reporting.mocks.generators;

import com.reporting.mocks.model.RiskRunRequest;
import com.reporting.mocks.model.RiskRunResult;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RiskRunGenerator {
    public static RiskRunResult generate(RiskRunRequest riskRun) {
        List<Risk> risks = new ArrayList<Risk>();
        Collection<Trade> trades = TradeStore.getStore().getTradePopulation(riskRun.getTradePopulationId()).getTrades();
        for(RiskType rt : riskRun.getRisks()) {
            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(rt);
            if (riskGenerator != null) {
                for (Trade t : trades) {
                    risks.add(riskGenerator.generate(riskRun, t));
                }
            }
        }
        return new RiskRunResult(riskRun, risks);
    }
}
