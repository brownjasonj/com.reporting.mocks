package com.reporting.mocks.process;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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

    public ProcessSimulator addProcess(ProcessSimulator completeProcess) {
        return this.processes.put(completeProcess.getPricingGroupId().getName(), completeProcess);
    }

    public ProcessSimulator getProcess(String name) {
        if (this.processes.containsKey(name))
            return this.processes.get(name);
        else
            return null;
    }

}
