package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.risks.Gamma;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.process.risks.RiskRequest;

import java.util.Random;

public class GammaGenerator implements IRiskGenerator<Gamma> {
    @Override
    public RiskType getRiskType() {
        return RiskType.GAMMA;
    }

    @Override
    public Gamma generate(RiskRequest riskRequest, Trade trade) {
        return new Gamma(
                riskRequest.getCalculationContext().get(this.getRiskType()),
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
