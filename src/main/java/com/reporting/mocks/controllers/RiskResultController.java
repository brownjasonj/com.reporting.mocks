package com.reporting.mocks.controllers;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.id.CalculationContextId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.persistence.IRiskResultStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class RiskResultController {
    @Autowired
    IRiskResultStore riskResultStore;

    @GetMapping("/riskrun")
    public List<RiskResult> getAllRiskResults() {
        return this.riskResultStore.getAll();
    }

    @GetMapping("/riskrun/riskrunId/{pricingGroupName}/{uuid}")
    public List<RiskResult> getRiskResultByRiskRunId(@PathVariable String pricingGroupName, @PathVariable UUID uuid) {
        return this.riskResultStore.getAllByRiskRunId(new RiskRunId(pricingGroupName, uuid));
    }
    
}
