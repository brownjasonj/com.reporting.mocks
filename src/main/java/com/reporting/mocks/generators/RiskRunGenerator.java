package com.reporting.mocks.generators;

import com.reporting.mocks.model.RiskRun;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

import java.util.ArrayList;
import java.util.List;

public class RiskRunGenerator {
    public static List<Risk> generate(RiskRun riskRun) {
        List<Risk> risks = new ArrayList<Risk>();
        for(RiskType rt : riskRun.getRisks()) {
            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(rt);
            if (riskGenerator != null) {
                for (Trade t : riskRun.getTrades()) {
                    risks.add(riskGenerator.generate(riskRun, t));
                }
            }
        }
        return risks;
    }
}
