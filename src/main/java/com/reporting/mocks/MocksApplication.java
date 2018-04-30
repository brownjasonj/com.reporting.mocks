package com.reporting.mocks;

import com.reporting.mocks.configuration.ConfigurationManager;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.process.CompleteProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MocksApplication {

	public static void main(String[] args) {

		PricingGroupConfig pgc = ConfigurationManager.getConfigurationManager().getPriceingGroupConfig("FXDesk");
		CompleteProcess.addProcess(new CompleteProcess(pgc));

		SpringApplication.run(MocksApplication.class, args);
	}
}
