package com.reporting.mocks.generators;

import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.model.RiskResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RiskRunGenerator {
    protected static List<RiskResult> fragmentResults(List<Risk> risks, int fragSize, RiskRequest riskRequest) {
        List<RiskResult> results = new ArrayList<>();
        int fragCount = risks.size() / fragSize + ((risks.size() % fragSize != 0 ? 1 : 0));

        for (int frag = 0; frag < risks.size(); frag += fragSize) {
            int fragNo = frag / fragSize;
            int indexStart = frag;
            int indexFinish = frag + fragSize > risks.size() ? risks.size() : frag + fragSize - 1;
            List<Risk> fragResult = risks.subList(frag, indexFinish);
            RiskResult riskResult = new RiskResult(riskRequest.getCalculationId(),
                    riskRequest.getTradePopulationId(),
                    riskRequest.getRiskRunId(),
                    fragCount,
                    fragNo,
                    fragResult);
            results.add(riskResult);
        }

        return results;
    }

    public static List<RiskResult> generate(CalculationContext calculationContext, TradePopulation tradePopulation, List<RiskType> riskTypes, int fragmentSize) {
        List<RiskResult> results = new ArrayList<>();
        Collection<Trade> trades = tradePopulation.getAllTrades();
        for(RiskType riskType : riskTypes) {
            List<Risk> risks = new ArrayList<>();
            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
            RiskRequest riskRequest = new RiskRequest(calculationContext.getId(), calculationContext.get(riskType), tradePopulation.getId());
            if (riskGenerator != null) {
                for (Trade t : trades) {
                    risks.add(riskGenerator.generate(riskRequest, t));
                }
            }

            results.addAll(RiskRunGenerator.fragmentResults(risks, fragmentSize, riskRequest));
        }

        return results;
    }
}
