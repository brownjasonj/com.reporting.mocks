package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.risks.Vega;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.Underlying;
import com.reporting.mocks.process.risks.RiskRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class VegaGenerator implements IRiskGenerator<Vega> {

    @Override
    public RiskType getRiskType() {
        return RiskType.VEGA;
    }

    @Override
    public Vega generate(RiskRequest riskRequest, Trade trade) {
        Random rand = new Random();
        Vega vega = new Vega(riskRequest.getCalculationId(),
                riskRequest.getCalculationContext().get(this.getRiskType()),
                riskRequest.getTradePopulationId(),
                riskRequest.getRiskRunId(), trade.getBook(), trade.getTcn());

        ArrayList<String> timeBuckets = new ArrayList<String>(
                Arrays.asList("OIS", "1mth", "3mth", "6mth", "1yr", "2yr", "3yr"));

        vega.addTimeBuckets(timeBuckets);

        Underlying underlying = trade.getUnderlying();
        for(int underlyignComponentIndex = 0; underlyignComponentIndex < underlying.getComponenetCount(); underlyignComponentIndex++) {
            String underlyingComponentName = underlying.getComponentName(underlyignComponentIndex);
            ArrayList<Double> timeBucketValues = new ArrayList<>(timeBuckets.size());
            for(int i = 0; i < timeBuckets.size(); i++) {
                timeBucketValues.add(i, rand.nextDouble());
            }
            vega.addTimeBuckValues(underlyingComponentName, timeBucketValues);
        }
        return vega;
    }

    @Override
    public int calcTimeEstimate(TradeType tradeType) {
        return 0;
    }
}
