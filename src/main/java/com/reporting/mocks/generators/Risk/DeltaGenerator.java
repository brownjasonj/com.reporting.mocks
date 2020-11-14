package com.reporting.mocks.generators.Risk;

import java.util.Random;

import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Delta;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

public class DeltaGenerator implements IRiskGeneratorLite<Delta> {
    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    @Override
    public Delta generate(MarketEnvId marketEnvId, Trade trade) {
        return new Delta(
                marketEnvId,
                trade.getBook(),
                trade.getTcn(),
                trade.getUnderlying1(),
                new Random().nextDouble() * trade.getUnderlying1Amount());
    }

    public int calcTimeEstimate(TradeType tradeType) {
        switch(tradeType) {
            case BarrierOption:
                return 20;
            case Forward:
                return 2;
            case Spot:
                return 1;
            case Swap:
                return 3;
            case VanillaOption:
                return 5;
            case Balance:
                return 0;
            case Payment:
                return 0;
        }
        return 0;
    }
}
