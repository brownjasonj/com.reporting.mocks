package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Pv;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.RiskRunRequest;

import javax.validation.constraints.Null;

import java.util.Random;

import static com.reporting.mocks.model.risks.RiskType.PV;


public class PvGenerator implements IRiskGenerator {
    @Override
    public Risk generate(RiskRequest riskRequest, Trade trade) {
        try {
            return new Pv(riskRequest.getCalculationId(),
                    riskRequest.getCalculationContext().get(this.getRiskType()),
                    riskRequest.getTradePopulationId(),
                    riskRequest.getRiskRunId(),
                    trade.getBook(),
                    trade.getTcn(),
                    trade.getUnderlying1(),
                    new Random().nextDouble() * trade.getQuantity());
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
        }
        return 0;
    }
}
