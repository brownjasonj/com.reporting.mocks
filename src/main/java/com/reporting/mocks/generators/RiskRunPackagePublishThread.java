package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.persistence.ICalculationContextStore;
import com.reporting.mocks.persistence.IRiskResultStore;
import com.reporting.mocks.persistence.ITradeStore;

import java.util.concurrent.BlockingQueue;

public class RiskRunPackagePublishThread implements Runnable {
    protected BlockingQueue<Risk> riskQueue;

    public RiskRunPackagePublishThread(
            BlockingQueue<Risk> riskQueue,
            PricingGroupConfig appConfig,
            ICalculationContextStore ICalculationContextStore,
            ITradeStore tradeStore,
            RiskRunPublisher riskRunPublisher,
            IRiskResultStore riskResultStore
    ) {

    }

    @Override
    public void run() {

    }
}
