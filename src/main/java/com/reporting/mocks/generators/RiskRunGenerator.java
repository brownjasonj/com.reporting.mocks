package com.reporting.mocks.generators;

import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.persistence.TradeStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RiskRunGenerator {
    public static RiskRunResult generate(RiskRunRequest riskRun) {
//        List<Risk> risks = new ArrayList<Risk>();
//        Collection<Trade> trades = TradeStore.getStore().getTradePopulation(riskRun.getTradePopulationId()).getTrades();
//        for(RiskType rt : riskRun.getRisks()) {
//            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(rt);
//            if (riskGenerator != null) {
//                for (Trade t : trades) {
//                    risks.add(riskGenerator.generate(riskRun, t));
//                }
//            }
//        }
//        return new RiskRunResult(riskRun, risks);

        return null;
    }

    public static List<RiskRunResult> generate(TradePopulation tradePopulation, RiskRunRequest riskRunRequest) {
        List<Risk> risks = new ArrayList<>();
        List<RiskRunResult> riskRunResults = new ArrayList<>();
        Collection<Trade> trades = tradePopulation.getTrades();
        IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskRunRequest.getRiskType());
        if (riskGenerator != null) {
            for (Trade t : trades) {
                risks.add(riskGenerator.generate(riskRunRequest, t));
            }
        }

        int fragSize = riskRunRequest.getFragmentSize();
        int fragCount = risks.size() / fragSize + ((risks.size() % fragSize != 0 ? 1 : 0));

        for(int frag = 0; frag < risks.size(); frag += riskRunRequest.getFragmentSize()) {
            //  public RiskRunResult(ResultKind kind, int fragmentCount, int fragmentNo, RiskRunRequest request, List<Risk> risks) {

            int fragNo = frag / fragSize;
            int indexStart = frag;
            int indexFinish = frag + fragSize > risks.size() ? risks.size() - 1 : frag + fragSize - 1;
            RiskRunResult rrr = new RiskRunResult(ResultKind.Fragment, fragCount, fragNo, riskRunRequest, risks.subList(frag, indexFinish));
            riskRunResults.add(rrr);
        }

        return riskRunResults;
     }
}
