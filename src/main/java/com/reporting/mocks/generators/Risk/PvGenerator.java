package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Pv;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

import static com.reporting.mocks.model.risks.RiskType.PV;


public class PvGenerator implements IRiskGenerator {
    @Override
    public Risk generate(RiskRequest riskRequest, Trade trade) {
        return new Pv(riskRequest.getCalculationId(),
                riskRequest.getMarketEnvId(),
                riskRequest.getTradePopulationId(),
                riskRequest.getRiskRunId(),
                trade.getBook(),
                trade.getTcn(),
                trade.getUnderlying().getUnderlyingCurrency());
    }


    @Override
    public RiskType getRiskType() {
        return PV;
    }
}
