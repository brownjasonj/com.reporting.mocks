package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.Gamma;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.Random;

public class GammaGenerator implements IRiskGeneratorLite<Gamma> {
    @Override
    public RiskType getRiskType() {
        return RiskType.GAMMA;
    }

    @Override
    public Gamma generate(MarketEnvId marketEnvId, Trade trade) {
        return new Gamma(
                marketEnvId,
                trade.getBook(),
                trade.getTcn(),
                trade.getUnderlying1(),
                new Random().nextDouble() * trade.getUnderlying1Amount()
        );
    }

    @Override
    public int calcTimeEstimate(TradeType tradeType) {
        return 0;
    }
}
