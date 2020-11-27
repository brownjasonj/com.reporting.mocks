package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Rho;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.Random;

public class RhoGenerator implements IRiskGeneratorLite<Rho> {
    @Override
    public RiskType getRiskType() { return RiskType.RHO; }

    @Override
    public Rho generate(MarketEnvId marketEnvId, Trade trade) {
        try {
            return new Rho(
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
    public int calcTimeEstimate(TradeType tradeType) {
        return 0;
    }
}
