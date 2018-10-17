package com.reporting.mocks.generators;

import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.TradePopulationId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultStore;
import com.reporting.mocks.persistence.ITradeStore;
import com.reporting.mocks.process.endofday.EndofDayEventTimerThread;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.process.risks.RiskRunRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RiskRunGeneratorThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( EndofDayEventTimerThread.class.getName() );
    protected BlockingQueue<RiskRunRequest> riskRunRequestQueue;
    protected ICalculationContextStore calculationContextStore;
    protected ITradeStore tradeStore;
    protected RiskRunPublisher riskRunPublisher;
    protected IRiskResultStore riskResultStore;

    public RiskRunGeneratorThread(BlockingQueue<RiskRunRequest> riskRunRequestQueue,
                                  ICalculationContextStore ICalculationContextStore,
                                  ITradeStore tradeStore,
                                  RiskRunPublisher riskRunPublisher,
                                  IRiskResultStore riskResultStore
                                  ) {
        this.riskRunRequestQueue = riskRunRequestQueue;
        this.calculationContextStore = ICalculationContextStore;
        this.tradeStore = tradeStore;
        this.riskResultStore = riskResultStore;
        this.riskRunPublisher = riskRunPublisher;
    }


    private void createAndPublishRiskResult(RiskRunRequest riskRunRequest, int fragmentCount, int fragmentNo, List<Risk> risks) {
        RiskResult riskResult = new RiskResult(
                riskRunRequest.getCalculationId(),
                riskRunRequest.getTradePopulationId(),
                riskRunRequest.getRiskRunId(),
                fragmentCount,
                fragmentNo,
                risks,
                riskRunRequest.isDeleteEvent());

        // persist the riskResult for future use
        this.riskResultStore.add(riskResult);

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
    @Override
    public void run() {
        int maxTradeCountPerFragment = 100;
        try {
            while(true) {
                RiskRunRequest riskRunRequest = this.riskRunRequestQueue.take();
                TradePopulationId tradePopulationId = riskRunRequest.getTradePopulationId();
                ArrayList<Trade> trades = null;
                List<RiskType> riskTypes = riskRunRequest.getRisksToRun();
                int fragmentCount = riskTypes.size();
                int tradeFragmentsWithMaxTrades = 0;
                int finalFragmentTradeCount = 0;
                if (riskRunRequest.isSingleTrade()) {
                    trades = new ArrayList<>(Arrays.asList(riskRunRequest.getTrade()));
                    finalFragmentTradeCount = 1;
                }
                else {
                    trades = new ArrayList<>(this.tradeStore.getTradePopulation(tradePopulationId).getAllTrades());
                    int tradeCount = trades.size();
                    finalFragmentTradeCount = tradeCount % maxTradeCountPerFragment;
                    tradeFragmentsWithMaxTrades = tradeCount/ maxTradeCountPerFragment;
                    fragmentCount = fragmentCount * (tradeFragmentsWithMaxTrades + (finalFragmentTradeCount > 0 ? 1 : 0));
                }

                CalculationContext calculationContext = this.calculationContextStore.get(riskRunRequest.getCalculationId().getId());

                int fragmentNo = 0;
                for(RiskType riskType : riskTypes) {
                    IRiskGenerator<? extends Risk> riskGenerator = RiskGeneratorFactory.getGenerator(riskType);
                    RiskRequest riskRequest = new RiskRequest(calculationContext, tradePopulationId);

                    if (riskGenerator != null) {
                        int tradeFragmentNo = 0;
                        for (tradeFragmentNo = 0; tradeFragmentNo < tradeFragmentsWithMaxTrades; tradeFragmentNo++) {
                            List<Risk> risks = new ArrayList<>();
                            for (int tradeInFragNo = 0; tradeInFragNo < maxTradeCountPerFragment; tradeInFragNo++) {
                                Trade trade = trades.get((tradeFragmentNo * maxTradeCountPerFragment) + tradeInFragNo);
                                Risk risk = riskGenerator.generate(riskRequest, trade);
                                if (risk != null)
                                    risks.add(risk);
                            }
                            fragmentNo++;
                            createAndPublishRiskResult(riskRunRequest, fragmentCount, fragmentNo, risks);
                        }
                        List<Risk> risks = new ArrayList<>();
                        for (int tradeInFragNo = 0; tradeInFragNo < finalFragmentTradeCount; tradeInFragNo++) {
                            Trade trade = trades.get((tradeFragmentNo * maxTradeCountPerFragment) + tradeInFragNo);
                            Risk risk = riskGenerator.generate(riskRequest, trade);
                            if (risk != null)
                                risks.add(risk);
                        }
                        fragmentNo++;
                        createAndPublishRiskResult(riskRunRequest, fragmentCount, fragmentNo, risks);
                    }
                }
            }
        }
        catch (InterruptedException ie) {
            LOGGER.log( Level.FINE, "thread interrupted");
        }

    }
}
