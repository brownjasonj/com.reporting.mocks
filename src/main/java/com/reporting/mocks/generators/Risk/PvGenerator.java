package com.reporting.mocks.generators.Risk;

import static com.reporting.mocks.model.risks.RiskType.PV;

import java.util.Random;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.risks.Pv;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.process.risks.RiskRequest;


public class PvGenerator implements IRiskGenerator<Pv> {
    @Override
    public Pv generate(RiskRequest riskRequest, Trade trade) {
        try {
            return new Pv(riskRequest.getCalculationContext().get(this.getRiskType()),
                    trade.getBook(),
                    trade.getTcn(),
                    trade.getUnderlying1(),
                    new Random().nextDouble() * trade.getUnderlying1Amount());
        }
        catch (NullPointerException npe) {
            return null;
        }
    }


    @Override
    public RiskType getRiskType() {
        return PV;
    }

    @Override
    public int calcTimeEstimate(TradeType tradeType) {
        switch(tradeType) {
            case BarrierOption:
                return 10;
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
