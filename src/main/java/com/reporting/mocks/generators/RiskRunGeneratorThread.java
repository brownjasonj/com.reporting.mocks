package com.reporting.mocks.generators;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.CalculationContextStore;
import com.reporting.mocks.persistence.TradeStore;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RiskRunGeneratorThread implements Runnable {
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected CalculationContextStore calculationContextStore;
    protected TradeStore tradeStore;
    protected RiskRunPublisher riskRunPublisher;

    public RiskRunGeneratorThread(BlockingQueue<RiskRunRequest> riskRunRequestQueue,
                                  CalculationContextStore calculationContextStore,
                                  TradeStore tradeStore,
                                  RiskRunPublisher riskRunPublisher
                                  ) {
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.calculationContextStore = calculationContextStore;
        this.tradeStore = tradeStore;
        this.riskRunPublisher = riskRunPublisher;
    }


    @Override
    public void run() {
        try {
            while(true) {
                RiskRunRequest riskRunRequest = this.riskRunRequestQueue.take();
                TradePopulationId tradePopulationId = riskRunRequest.getTradePopulationId();
                Collection<Trade> trades = null;
                List<RiskType> riskTypes = riskRunRequest.getRisksToRun();
                int fragmentCount = riskTypes.size();
                if (riskRunRequest.isSingleTrade()) {
                    trades = new ArrayList<>(Arrays.asList(riskRunRequest.getTrade()));
                }
                else {
                    trades = this.tradeStore.getTradePopulation(tradePopulationId.getId()).getAllTrades();
                }

                CalculationContext calculationContext = this.calculationContextStore.get(riskRunRequest.getCalculationId().getId());
                for(int fragment = 0; fragment < fragmentCount; fragment++) {
                    List<Risk> risks = new ArrayList<>();
                    RiskType riskType = riskTypes.get(fragment);
                    IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
                    RiskRequest riskRequest = new RiskRequest(calculationContext.getId(), calculationContext.get(riskType), tradePopulationId);

                    if (riskGenerator != null) {
                        for (Trade t : trades) {
                            risks.add(riskGenerator.generate(riskRequest, t));
                        }
                    }

                    RiskResult riskResult = new RiskResult(
                            riskRunRequest.getCalculationId(),
                            riskRunRequest.getTradePopulationId(),
                            riskRunRequest.getRiskRunId(),
                            fragmentCount,
                            fragment,
                            risks,
                            riskRunRequest.isDeleteEvent());

                    switch (riskRunRequest.getRiskRunType()) {
                        case EndOfDay:
                            riskRunPublisher.publishEndofDayRiskRun(riskResult);
                            break;
                        case OnDemand:
                        case Intraday:
                            riskRunPublisher.publishIntradayRiskRun(riskResult);
                            break;
                        case IntradayTick:
                            riskRunPublisher.publishIntradayTick(riskResult);
                            break;
                        default:
                    }
                }

            }
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }
}
