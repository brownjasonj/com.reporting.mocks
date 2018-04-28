package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGenerator;
import com.reporting.mocks.model.RiskRun;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.Delta;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.underlying.Currency;

public class DeltaGenerator implements IRiskGenerator<Delta>{
    @Override
    public RiskType getRiskType() {
        return RiskType.DELTA;
    }

    @Override
    public Delta generate(RiskRun riskRun, Trade trade) {
        return new Delta(riskRun.getId(), trade.getTcn(), new Currency("EURUSD"));
    }
}
