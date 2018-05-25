package com.reporting.mocks.controllers;

import com.reporting.mocks.process.risks.RiskResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskRunController {

    @RequestMapping(method = { RequestMethod.GET }, value = { "/RiskResult/{pricingGroupName}" }, produces = "application/json")
    public RiskResult getRiskRun(@PathVariable String pricingGroupName) {
        return null;
    }
}
