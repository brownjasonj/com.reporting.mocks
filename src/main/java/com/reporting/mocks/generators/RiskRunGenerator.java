package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.model.*;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.process.risks.ResultKind;
import com.reporting.mocks.process.risks.requests.*;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.response.SRRunResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RiskRunGenerator {
    protected PricingGroupConfig config;


    public static List<RiskRunResult> generate(TradePopulation tradePopulation, MTSRRiskRunRequest riskRunRequest) {
        List<Risk> risks = new ArrayList<>();
        List<RiskRunResult> riskRunResults = new ArrayList<>();
        Collection<Trade> trades = tradePopulation.getTrades();
        IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskRunRequest.getRiskType());
        if (riskGenerator != null) {
            for (Trade t : trades) {
                risks.add(riskGenerator.generate(riskRunRequest, t));
            }
        }

        riskRunResults.addAll(RiskRunGenerator.fragmentResults(risks, riskRunRequest.getFragmentSize(), riskRunRequest));

        return riskRunResults;
     }

     protected static List<RiskRunResult> fragmentResults(List<Risk> risks, int fragSize, RiskRunRequest riskRunRequest) {
         List<RiskRunResult> riskRunResults = new ArrayList<>();
         int fragCount = risks.size() / fragSize + ((risks.size() % fragSize != 0 ? 1 : 0));

         for (int frag = 0; frag < risks.size(); frag += fragSize) {
             int fragNo = frag / fragSize;
             int indexStart = frag;
             int indexFinish = frag + fragSize > risks.size() ? risks.size() - 1 : frag + fragSize - 1;
             RiskRunResult rrr = new MRRunResponse(ResultKind.Fragment, fragCount, fragNo, riskRunRequest, risks.subList(frag, indexFinish));
             riskRunResults.add(rrr);
         }

         return riskRunResults;
     }

     public static List<RiskRunResult> generate(TradePopulation tradePopulation, MTMRRiskRunRequest riskRunRequest) {
        List<RiskRunResult> riskRunResults = new ArrayList<>();
        Collection<Trade> trades = tradePopulation.getTrades();
        for(RiskType riskType : riskRunRequest.getRiskTypes()) {
            List<Risk> risks = new ArrayList<>();
            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
            if (riskGenerator != null) {
                for (Trade t : trades) {
                    risks.add(riskGenerator.generate(riskRunRequest, t));
                }
            }

            riskRunResults.addAll(RiskRunGenerator.fragmentResults(risks, riskRunRequest.getFragmentSize(), riskRunRequest));
        }

        return riskRunResults;
    }

    public static List<RiskRunResult> generate(STMRRiskRunRequest riskRunRequest) {
        List<RiskRunResult> riskRunResults = new ArrayList<>();
        for(RiskType riskType : riskRunRequest.getRiskTypes()) {
            IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
            if (riskGenerator != null) {
                Risk risk = riskGenerator.generate(riskRunRequest, riskRunRequest.getTrade());
                riskRunResults.add(new SRRunResponse(ResultKind.Complete, riskRunRequest, risk));
            }
        }
        return riskRunResults;

    }

    public static List<RiskRunResult> generate(STSRRiskRunRequest riskRunRequest) {
        List<RiskRunResult> riskRunResults = new ArrayList<>();
        IRiskGenerator riskGenerator = RiskGeneratorFactory.getGenerator(riskRunRequest.getRiskType());
        if (riskGenerator != null) {
            Risk risk = riskGenerator.generate(riskRunRequest, riskRunRequest.getTrade());
            riskRunResults.add(new SRRunResponse(ResultKind.Complete, riskRunRequest, risk));
        }
        return riskRunResults;
    }
}
