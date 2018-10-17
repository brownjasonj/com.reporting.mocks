package com.reporting.mocks;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.persistence.*;
import com.reporting.mocks.persistence.mongo.RiskResultRunRepository;
import com.reporting.mocks.process.ProcessFactory;
import com.reporting.mocks.process.ProcessSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MocksApplication implements CommandLineRunner {
//	@Autowired
//	ApplicationConfig applicationConfig;
//
//	@Autowired
//    ProcessFactory processFactory;
//
//	@Autowired
//    Configurations configurations;
//
//	@Autowired
//    IPersistenceStoreFactory<ICalculationContextStore> calculationContextStoreFactory;
//
//	@Autowired
//    IPersistenceStoreFactory<IMarketStore> marketStoreFactory;
//
//	@Autowired
//    IPersistenceStoreFactory<ITradeStore> mongoTradeStoreFactory;
//
//	@Autowired
//    IRiskResultStore riskResultStore;

	public static void main(String[] args) {
		SpringApplication.run(MocksApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
//        for (PricingGroupConfig config : this.configurations.getPricingGroups()) {
//            String pricingGroupName = config.getPricingGroupId().getName();
//            ITradeStore tradeStore = this.mongoTradeStoreFactory.create(config.getPricingGroupId());
//            IMarketStore marketStore = this.marketStoreFactory.create(config.getPricingGroupId());
//            ICalculationContextStore calculationContextStore = this.calculationContextStoreFactory.create(config.getPricingGroupId());
//
//            ProcessSimulator processSimulator = new ProcessSimulator(config, this.applicationConfig, calculationContextStore, marketStore, tradeStore, riskResultStore);
//            this.processFactory.addProcess(processSimulator);
//        }
    }
}
