package com.reporting.mocks.controllers;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.*;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.process.ProcessFactory;
import com.reporting.mocks.process.ProcessSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControlProcess {
    @Autowired
    ProcessFactory processFactory;

    @Autowired
    ApplicationConfig applicationConfig;

    @Autowired
    Configurations configurations;

    @Autowired
    IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory;

    @Autowired
    IPersistenceStoreFactory<IMarketStore> marketStoreFactory;

    @Autowired
    IPersistenceStoreFactory<ITradeStore> mongoTradeStoreFactory;

    @Autowired
    IResultPublisher resultPublisher;

    @Autowired
    IRiskResultStore riskResultStore;

    @Autowired
    IPersistenceAdmin persistenceAdmin;

    @Autowired
    public ControlProcess(ProcessFactory processFactory) {
        this.processFactory = processFactory;
    }

    @GetMapping("/controlprocess/start/{pricingGroupName}")
    public PricingGroupConfig startCompleteProcess(@PathVariable String pricingGroupName) {
        ProcessSimulator processSimulator = this.processFactory.getProcess(new PricingGroup(pricingGroupName));
        if (processSimulator != null) {
            return processSimulator.start();
        }
        else {
            PricingGroupConfig config = this.configurations.getPricingGroup(pricingGroupName);
            if (config != null) {
                processSimulator = this.processFactory.createProcess(this.applicationConfig,
                        config,
                        calculationContextStoreFactory,
                        marketStoreFactory,
                        mongoTradeStoreFactory,
                        riskResultStore,
                        resultPublisher);
                return processSimulator.start();
            }
            else
                return null;
        }
    }

    @GetMapping("/controlprocess/stop/{pricingGroupName}")
    public Boolean stopCompleteProcess(@PathVariable String pricingGroupName) {
        PricingGroup pricingGroup = new PricingGroup(pricingGroupName);
        ProcessSimulator processSimulator = this.processFactory.getProcess(pricingGroup);
        if (processSimulator != null) {
            processSimulator.stop();
            this.processFactory.deleteProcess(pricingGroup);
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping("/controlprocess/resetpersistence")
    public String resetPersistence() {
        if (this.processFactory.noSimulatorsRunning()) {
            persistenceAdmin.clearDataSets();
            return "All persisted datasets cleared";
        }
        else {
            return "Stop all simulators before proceeding";
        }
    }
}
