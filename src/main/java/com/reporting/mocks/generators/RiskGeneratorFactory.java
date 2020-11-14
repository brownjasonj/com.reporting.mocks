package com.reporting.mocks.generators;

import com.reporting.mocks.generators.Risk.DeltaGenerator;
import com.reporting.mocks.generators.Risk.GammaGenerator;
import com.reporting.mocks.generators.Risk.PvGenerator;
import com.reporting.mocks.generators.Risk.VegaGenerator;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

import java.util.HashMap;

public class RiskGeneratorFactory {
    private static HashMap<RiskType, IRiskGeneratorLite<? extends Risk>> liteGenerators = new HashMap<>();

    static {
        RiskGeneratorFactory.registerLiteGenerator(new PvGenerator());
        RiskGeneratorFactory.registerLiteGenerator(new DeltaGenerator());
        RiskGeneratorFactory.registerLiteGenerator(new VegaGenerator());
        RiskGeneratorFactory.registerLiteGenerator(new GammaGenerator());

    }


    private static void registerLiteGenerator(IRiskGeneratorLite<? extends Risk> riskGeneratorLite) {
        RiskGeneratorFactory.liteGenerators.put(riskGeneratorLite.getRiskType(), riskGeneratorLite);
    }

    public static IRiskGeneratorLite<? extends Risk> getGeneratorLite(RiskType riskType) {
        if (RiskGeneratorFactory.liteGenerators.containsKey(riskType)) {
            return RiskGeneratorFactory.liteGenerators.get(riskType);
        }
        else {
            return null;
        }
    }
}
