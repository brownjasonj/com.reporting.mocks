package com.reporting.mocks.generators.Risk;

import java.util.Random;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.risks.Delta;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.process.risks.RiskRequest;

public class DeltaGenerator implements IRiskGenerator<Delta>{
    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    @Override
    public Delta generate(RiskRequest riskRequest, Trade trade) {
        return new Delta(riskRequest.getCalculationContext().get(this.getRiskType()),
                trade.getBook(), trade.getTcn(), trade.getUnderlying1(),
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
