package com.reporting.mocks.model.dataviews.book;

import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;

import java.util.HashMap;
import java.util.UUID;

public class TcnRisksAll {
    UUID tcn;
    HashMap<RiskType, Risk> risks;

    public TcnRisksAll() {
        this.risks = new HashMap<>();
    }

    public TcnRisksAll(UUID tcn) {
        this();
        this.tcn = tcn;
    }

    public <T extends Risk> void setRisk(T r) {
        this.risks.put(r.getRiskType(), r);
    }

    public <T extends Risk> T getRisk(RiskType rt) {
        if (this.risks.containsKey(rt)) {
            return (T)this.risks.get(rt);
        }
        else {
            return null;
        }
    }

    public UUID getTcn() {
        return tcn;
    }


}
