package com.reporting.mocks;

import java.util.Arrays;

import com.google.gson.Gson;
import com.reporting.mocks.configuration.ApplicationConfig;
import com.reporting.mocks.configuration.Configurations;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.IMarketStore;
import com.reporting.mocks.interfaces.persistence.IPersistenceStoreFactory;
import com.reporting.mocks.interfaces.persistence.IRiskResultStore;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.process.ProcessFactory;
import com.reporting.mocks.process.ProcessSimulator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PostStartUp implements CommandLineRunner {
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
    
    private static final Logger logger = LoggerFactory.getLogger(PostStartUp.class);

    private Gson gson;

    @Autowired
    public PostStartUp(ProcessFactory processFactory) {
        this.processFactory = processFactory;
        this.gson = new Gson();
    }
    
    private PricingGroupConfig startCompleteProcess(String pricingGroupName) {
        PricingGroupConfig pricingGroupConfig = this.configurations.getPricingGroup(pricingGroupName);
        if (pricingGroupConfig != null) {
            ProcessSimulator processSimulator = this.processFactory.getProcess(pricingGroupConfig.getPricingGroupId());
            if (processSimulator != null) {
                return processSimulator.start();
            } else {
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
            }
        }
        return null;
    }

    @Override
    public void run(String...args) throws Exception {
        CommandLine commandLine;
         Option optionStart = Option.builder("start")
            .required(false)
            .argName("start")
            .hasArgs()
            .desc("Start simulators for given pricing groups")
            .build();
        
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(optionStart);

        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("start")) {
                String[] pricingGroups = commandLine.getOptionValues("start");
                for(String pricingGroup : pricingGroups) {
                    PricingGroupConfig pgc = startCompleteProcess(pricingGroup);
                    if (pgc == null) {
                        logger.error("Couldn't start PricingGroup: " + pricingGroup);
                    }
                    else {
                        logger.info("Started PricingGroup " + pricingGroup);
                        logger.info(this.gson.toJson(pgc));
                    }
                }
            }
        }
        catch (ParseException exception)
        {
            logger.error("Parse error: ");
            logger.error(exception.getMessage());
        }

        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
    }
}