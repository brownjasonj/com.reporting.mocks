package com.reporting.mocks.generators;

import com.reporting.mocks.generators.Risk.DeltaGenerator;
import com.reporting.mocks.generators.Risk.PvGenerator;
import com.reporting.mocks.generators.Risk.VegaGenerator;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

import java.util.HashMap;

public class RiskGeneratorFactory {
    private static HashMap<RiskType, IRiskGenerator<? extends Risk>> generators = new HashMap<>();

    static {
        RiskGeneratorFactory.register(new PvGenerator());
        RiskGeneratorFactory.register(new DeltaGenerator());
        RiskGeneratorFactory.register(new VegaGenerator());
    }

    private static void register(IRiskGenerator<? extends Risk> riskGenerator) {
        RiskGeneratorFactory.generators.put(riskGenerator.getRiskType(), riskGenerator);
    }

    public static IRiskGenerator<? extends Risk> getGenerator(RiskType riskType) {
        if (RiskGeneratorFactory.generators.containsKey(riskType)) {
            return RiskGeneratorFactory.generators.get(riskType);
        }
        else {
            return null;
        }
    }


}
