package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.process.risks.RiskRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Delta;
import com.reporting.mocks.model.risks.RiskType;

public class DeltaGenerator implements IRiskGenerator<Delta>{
    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    @Override
    public Delta generate(RiskRequest riskRequest, Trade trade) {
        return new Delta(riskRequest.getCalculationId(),
                riskRequest.getMarketEnvId(),
                riskRequest.getTradePopulationId(),
                riskRequest.getRiskRunId(), trade.getBook(), trade.getTcn(), trade.getUnderlying());
    }
}
