package com.reporting.mocks.process;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.*;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.PricingGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Scope
public class ProcessFactory {
    protected ConcurrentHashMap<String, ProcessSimulator> processes;

    ProcessFactory() {
        this.processes = new ConcurrentHashMap<>();
    }

    protected ProcessSimulator addProcess(ProcessSimulator completeProcess) {
        return this.processes.put(completeProcess.getPricingGroupId().getName(), completeProcess);
    }

    public ProcessSimulator deleteProcess(PricingGroup pricingGroup) {
        if (this.processes.containsKey(pricingGroup.getName()))
            return this.processes.remove(pricingGroup.getName());
        else
            return null;
    }
    public ProcessSimulator getProcess(PricingGroup pricingGroup) {
        if (this.processes.containsKey(pricingGroup.getName()))
            return this.processes.get(pricingGroup.getName());
        else
            return null;
    }

    public Boolean noSimulatorsRunning() {
        return processes.isEmpty();
    }

    public ProcessSimulator createProcess(ApplicationConfig applicationConfig,
                                          PricingGroupConfig config,
                                          IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory,
                                          IPersistenceStoreFactory<IMarketStore> marketStoreFactory,
                                          IPersistenceStoreFactory<ITradeStore> tradeStoreFactory,
                                          IRiskResultStore riskResultStore,
                                          IResultPublisher resultPublisher
                                          ) {
        String pricingGroupName = config.getPricingGroupId().getName();
        ITradeStore tradeStore = tradeStoreFactory.create(config.getPricingGroupId());
        IMarketStore marketStore = marketStoreFactory.create(config.getPricingGroupId());
        ICalculationContextStore calculationContextStore = calculationContextStoreFactory.create(config.getPricingGroupId());

        ProcessSimulator processSimulator = new ProcessSimulator(config,
                applicationConfig,
                calculationContextStore,
                marketStore,
                tradeStore,
                riskResultStore,
                resultPublisher);
        this.addProcess(processSimulator);
        return processSimulator;
    }
}
