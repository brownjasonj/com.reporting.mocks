package com.reporting.mocks.endpoints.ignite;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.dataviews.book.TcnRiskAggregate;
import com.reporting.mocks.model.dataviews.book.TcnRisksAll;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.process.risks.response.MRRunResponse;
import com.reporting.mocks.process.risks.response.RiskRunResult;
import com.reporting.mocks.process.risks.response.SRRunResponse;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;

import javax.cache.event.CacheEntryEvent;
import java.util.Random;
import java.util.UUID;

public class RiskRunIgnitePublisher implements RiskRunPublisher {
    protected PricingGroup pricingGroup;
    protected Ignite ignite;

    public RiskRunIgnitePublisher(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        Ignition.setClientMode(true);
        this.ignite = Ignition.start("examples/config/example-ignite.xml");

//        this.cache = ignite.getOrCreateCache("myCache");

    }

    @Override
    public void publish(RiskRunResult riskRunResult) {
        System.out.println("{Risk Result: (" + riskRunResult.getRequest().getType() + "): " + riskRunResult.getId() + " Risk: " + riskRunResult.getRequest() + " fragment: " + riskRunResult.getFragmentNo() + "/" + riskRunResult.getFragmentCount() + "}") ;

        String pricingGroupName = this.pricingGroup.getName();
        String marketId = riskRunResult.getRequest().getMarketEnvId().toString();
        String bookRisks = marketId + "/Risks";

        try {
            switch (riskRunResult.getSetKind()) {
                case MR: {
                    MRRunResponse mrrr = (MRRunResponse) riskRunResult;
                    for (Risk r : mrrr.getRisks()) {
                        UUID tcn = r.getTcn();
                        Double value = (new Random()).nextDouble();

                        String cacheNameRoot = "/" + pricingGroupName + "/" + r.getBookName() + "/" + bookRisks;
                        String tcnCacheName = cacheNameRoot + "/" + r.getRiskType()+ "/Tcn";
                        System.out.println("Writing to cache: " + tcnCacheName);
                        IgniteCache<UUID,Double> tcnRisk = ignite.getOrCreateCache(tcnCacheName);
                        System.out.println("Writing to cache: " + cacheNameRoot);
                        IgniteCache<String,Double> riskBook = ignite.getOrCreateCache(cacheNameRoot);

                        tcnRisk.put(tcn, value);
                        if (riskBook.containsKey(r.getRiskType().name())) {
                            Double oldValue = riskBook.get(r.getRiskType().name());
                            riskBook.getAndPut(r.getRiskType().name(), oldValue + value);
                        }
                        else {
                            riskBook.getAndPut(r.getRiskType().name(), 0.0);
                        }
                        System.out.println("{Cache: " + cacheNameRoot + ", RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + ", value: " + value + "}");

                    }
                }
                break;
                case SR: {
                    SRRunResponse srrr = (SRRunResponse) riskRunResult;
                    Risk r = srrr.getRisk();
                    UUID tcn = r.getTcn();
                    Double value = (new Random()).nextDouble();


                    String cacheNameRoot = "/" + pricingGroupName + "/" + r.getBookName() + "/" + bookRisks;
                    String tcnCacheName = cacheNameRoot + "/" + r.getRiskType()+ "/Tcn";
                    System.out.println("Writing to cache: " + tcnCacheName);
                    IgniteCache<UUID,Double> tcnRisk = ignite.getOrCreateCache(tcnCacheName);
                    System.out.println("Writing to cache: " + cacheNameRoot);
                    IgniteCache<String,Double> riskBook = ignite.getOrCreateCache(cacheNameRoot);


                    tcnRisk.put(tcn, value);
                    if (riskBook.containsKey(r.getRiskType().name())) {
                        Double oldValue = riskBook.get(r.getRiskType().name());
                        riskBook.getAndPut(r.getRiskType().name(), oldValue + value);
                    }
                    else {
                        riskBook.getAndPut(r.getRiskType().name(), 0.0);
                    }
                    System.out.println("{Cache: " + cacheNameRoot + ", RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + ", value: " + value + "}");

                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(CalculationContext calculationContext) {

    }

    @Override
    public void publish(MarketEnv marketEnv) {

    }
}
