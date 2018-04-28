package com.reporting.mocks.generators;

import com.reporting.mocks.generators.Risk.DeltaGenerator;
import com.reporting.mocks.generators.Risk.PvGenerator;
import com.reporting.mocks.model.risks.RiskType;

import java.util.HashMap;

public class RiskGeneratorFactory {
    private static HashMap<RiskType, IRiskGenerator> generators = new HashMap<>();

    static {
        RiskGeneratorFactory.register(new PvGenerator());
        RiskGeneratorFactory.register(new DeltaGenerator());
    }

    private static void register(IRiskGenerator riskGenerator) {
        RiskGeneratorFactory.generators.put(riskGenerator.getRiskType(), riskGenerator);
    }

    public static IRiskGenerator getGenerator(RiskType riskType) {
        if (RiskGeneratorFactory.generators.containsKey(riskType)) {
            return RiskGeneratorFactory.generators.get(riskType);
        }
        else {
            return null;
        }
    }


}
