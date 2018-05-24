package com.reporting.mocks.endpoints.ignite;

import com.google.gson.Gson;
import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.endpoints.RiskRunPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.MarketEnv;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.dataviews.book.TcnRiskAggregate;
import com.reporting.mocks.model.dataviews.book.TcnRisksAll;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Tcn;
import com.reporting.mocks.persistence.CalculationContextStoreFactory;
import com.reporting.mocks.process.risks.RiskResult;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.configuration.CollectionConfiguration;

import javax.cache.event.CacheEntryEvent;
import java.util.Random;
import java.util.UUID;

public class RiskRunIgnitePublisher implements RiskRunPublisher {
    protected PricingGroup pricingGroup;
    protected Ignite ignite;
    protected CollectionConfiguration colCfg;

    public RiskRunIgnitePublisher(PricingGroup pricingGroup) {
        this.pricingGroup = pricingGroup;
        Ignition.setClientMode(true);
        this.ignite = Ignition.start("examples/config/example-ignite.xml");

        this.colCfg = new CollectionConfiguration();
        this.colCfg.setCollocated(false);
    }


//    protected boolean shouldProcessFragment(RiskRunResult riskRunResult) {
//        //int fragCount = riskRunResult.getFragmentCount();
//        int fragNo = riskRunResult.getFragmentNo();
//        IgniteSet<Integer> set = this.ignite.set(
//                riskRunResult.getRequest().getId().toString(), // set name is the risk run id.
//                this.colCfg       // Collection configuration.
//        );
//        return !set.contains(fragNo);
//    }
//
//    protected boolean completedProcessing(RiskRunResult riskRunResult) {
//        int fragCount = riskRunResult.getFragmentCount();
//        IgniteSet<Integer> set = ignite.set(
//                riskRunResult.getRequest().getId().toString(), // set name is the risk run id.
//                this.colCfg       // Collection configuration.
//        );
//        return (set.size() == fragCount);
//    }
//
//    protected boolean completedProcessingFragment(RiskRunResult riskRunResult) {
//        int fragCount = riskRunResult.getFragmentCount();
//        int fragNo = riskRunResult.getFragmentNo();
//        IgniteSet<Integer> set = ignite.set(
//                riskRunResult.getRequest().getId().toString(), // set name is the risk run id.
//                this.colCfg       // Collection configuration.
//        );
//        set.add(fragNo);
//        return (set.size() == fragCount);
//    }

//    @Override
//    public void publish(RiskRunResult riskRunResult) {
//        System.out.println("{Risk Result: (" + riskRunResult.getRequest().getType() + "): " + riskRunResult.getId() + " Risk: " + riskRunResult.getRequest() + " fragment: " + riskRunResult.getFragmentNo() + "/" + riskRunResult.getFragmentCount() + "}") ;
//
//        if (shouldProcessFragment(riskRunResult)) {
//            String pricingGroupName = this.pricingGroup.getName();
//            //String marketId = riskRunResult.getRequest().getMarketEnvId().toString();
//
//            try {
//                switch (riskRunResult.getSetKind()) {
//                    case MR: {
//                        MRRunResponse mrrr = (MRRunResponse) riskRunResult;
//                        for (Risk r : mrrr.getRisks()) {
//                            Tcn tcn = r.getTcn();
//                            Double value = (new Random()).nextDouble();
//
//                            CalculationContext context = CalculationContextStoreFactory.get(riskRunResult.getRequest().getCalculationContextUri());
//                            String marketId = context.get(r.getRiskType()).getId().toString();
//
//                            String cacheNameRoot = "/" + marketId + "/" + pricingGroupName + "/" + r.getBookName() + "/Risks";
//                            String tcnCacheName = cacheNameRoot + "/" + r.getRiskType() + "/Tcn";
//                            System.out.println("Writing to cache: " + tcnCacheName);
//                            IgniteCache<Tcn, Double> tcnRisk = ignite.getOrCreateCache(tcnCacheName);
//                            System.out.println("Writing to cache: " + cacheNameRoot);
//                            IgniteCache<String, Double> riskBook = ignite.getOrCreateCache(cacheNameRoot);
//
//                            tcnRisk.put(tcn, value);
//                            if (riskBook.containsKey(r.getRiskType().name())) {
//                                Double oldValue = riskBook.get(r.getRiskType().name());
//                                riskBook.getAndPut(r.getRiskType().name(), oldValue + value);
//                            } else {
//                                riskBook.getAndPut(r.getRiskType().name(), 0.0);
//                            }
//                            System.out.println("{Cache: " + cacheNameRoot + ", RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + ", value: " + value + "}");
//                        }
//
//                    }
//                    break;
//                    case SR: {
//                        SRRunResponse srrr = (SRRunResponse) riskRunResult;
//                        Risk r = srrr.getRisk();
//                        Tcn tcn = r.getTcn();
//                        Double value = (new Random()).nextDouble();
//
//                        CalculationContext context = CalculationContextStoreFactory.get(riskRunResult.getRequest().getCalculationContextUri());
//                        String marketId = context.get(r.getRiskType()).getId().toString();
//
//                        String cacheNameRoot = "/" + marketId + "/" + pricingGroupName + "/" + r.getBookName() + "/Risks";
//                        String tcnCacheName = cacheNameRoot + "/" + r.getRiskType() + "/Tcn";
//                        System.out.println("Writing to cache: " + tcnCacheName);
//                        IgniteCache<Tcn, Double> tcnRisk = ignite.getOrCreateCache(tcnCacheName);
//                        System.out.println("Writing to cache: " + cacheNameRoot);
//                        IgniteCache<String, Double> riskBook = ignite.getOrCreateCache(cacheNameRoot);
//
//
//                        tcnRisk.put(tcn, value);
//                        if (riskBook.containsKey(r.getRiskType().name())) {
//                            Double oldValue = riskBook.get(r.getRiskType().name());
//                            riskBook.getAndPut(r.getRiskType().name(), oldValue + value);
//                        } else {
//                            riskBook.getAndPut(r.getRiskType().name(), 0.0);
//                        }
//                        System.out.println("{Cache: " + cacheNameRoot + ", RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + ", value: " + value + "}");
//
//                    }
//                    break;
//                    default:
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (completedProcessingFragment(riskRunResult)) {
//                System.out.println("Completed processing all fragments: " + riskRunResult.getFragmentCount());
//            }
//        }
//    }

    @Override
    public void publish(CalculationContext calculationContext) {
//        Gson gson = new Gson();
//        String cacheName = "/calculationcontext/" + calculationContext.getPricingGroup().getName();
//        String json = gson.toJson(calculationContext);
//        System.out.println("Writing to cache: " + cacheName + ": " + json);
//        IgniteCache<UUID, String> cache = ignite.getOrCreateCache(cacheName);
//        cache.put(calculationContext.getId(), json);
    }

    @Override
    public void publish(MarketEnv marketEnv) {
//        Gson gson = new Gson();
//        String cacheName = "/market/" + marketEnv.getPricingGroup().getName();
//        String json = gson.toJson(marketEnv);
//        System.out.println("Writing to cache: " + cacheName + ": " + json);
//        IgniteCache<UUID, String> cache = ignite.getOrCreateCache(cacheName);
//        cache.put(marketEnv.getId(), json);
    }

    @Override
    public void publish(RiskResult riskResult) {

    }
}
