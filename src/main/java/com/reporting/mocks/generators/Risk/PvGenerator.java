package com.reporting.mocks.generators.Risk;

import static com.reporting.mocks.model.risks.RiskType.PV;

import java.util.Random;

import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Pv;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;


public class PvGenerator implements IRiskGeneratorLite<Pv> {
    @Override
    public Pv generate(MarketEnvId marketEnvId, Trade trade) {
        try {
            return new Pv(
                    marketEnvId,
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
