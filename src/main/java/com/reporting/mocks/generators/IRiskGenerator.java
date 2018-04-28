package com.reporting.mocks.generators;

import com.reporting.mocks.model.RiskRun;
import com.reporting.mocks.model.Trade;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

public interface IRiskGenerator <R extends Risk> {
    public RiskType getRiskType();
    public R generate(RiskRun riskRun, Trade trade);
}
