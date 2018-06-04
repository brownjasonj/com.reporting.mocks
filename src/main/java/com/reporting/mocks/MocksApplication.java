package com.reporting.mocks;

import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.ConfigurationManager;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.process.CompleteProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MocksApplication implements CommandLineRunner {
	@Autowired
	ApplicationConfig applicationConfig;

	public static void main(String[] args) {
		SpringApplication.run(MocksApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        for (PricingGroupConfig config : ConfigurationManager.getConfigurationManager().getConfig().getPricingGroups()) {
            CompleteProcess.addProcess(new CompleteProcess(config, applicationConfig));
        }
    }
}
