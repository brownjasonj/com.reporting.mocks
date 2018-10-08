package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.process.CompleteProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControlProcess {

    @GetMapping("/controlprocess/start/{pricingGroupName}")
    public Boolean startCompleteProcess(@PathVariable String pricingGroupName) {
        CompleteProcess completeProcess = CompleteProcess.getProcess(pricingGroupName);

        if (completeProcess != null) {
            completeProcess.start();
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping("/controlprocess/stop/{pricingGroupName}")
    public Boolean stopCompleteProcess(@PathVariable String pricingGroupName) {
        CompleteProcess completeProcess = CompleteProcess.getProcess(pricingGroupName);
        if (completeProcess != null) {
            completeProcess.stop();
            return true;
        }
        else {
            return false;
        }
    }
}
