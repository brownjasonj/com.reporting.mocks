package com.reporting.mocks.controllers;

import com.reporting.mocks.process.ProcessFactory;
import com.reporting.mocks.process.ProcessSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControlProcess {
    @Autowired
    ProcessFactory processFactory;

    @Autowired
    public ControlProcess(ProcessFactory processFactory) {
        this.processFactory = processFactory;
    }

    @GetMapping("/controlprocess/start/{pricingGroupName}")
    public Boolean startCompleteProcess(@PathVariable String pricingGroupName) {
        ProcessSimulator processSimulator = this.processFactory.getProcess(pricingGroupName);

        if (processSimulator != null) {
            processSimulator.start();
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping("/controlprocess/stop/{pricingGroupName}")
    public Boolean stopCompleteProcess(@PathVariable String pricingGroupName) {
        ProcessSimulator processSimulator = this.processFactory.getProcess(pricingGroupName);
        if (processSimulator != null) {
            processSimulator.stop();
            return true;
        }
        else {
            return false;
        }
    }
}
