package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.process.risks.requests.RiskRunRequest;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.risks.Delta;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.underlying.Currency;

public class DeltaGenerator implements IRiskGenerator<Delta>{
    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    @Override
    public Delta generate(RiskRunRequest riskRun, Trade trade) {
        return new Delta(riskRun.getId(), trade.getTcn(), trade.getUnderlying());
    }
}
