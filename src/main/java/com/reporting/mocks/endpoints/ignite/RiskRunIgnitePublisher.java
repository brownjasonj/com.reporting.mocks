package com.reporting.mocks.endpoints.ignite;

import com.reporting.mocks.endpoints.RiskRunPublisher;
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
import java.util.UUID;

public class RiskRunIgnitePublisher implements RiskRunPublisher {
    protected Ignite ignite;
    protected IgniteCache<UUID, Risk> cache;


    public RiskRunIgnitePublisher() {
        Ignition.setClientMode(true);
        this.ignite = Ignition.start("examples/config/example-ignite.xml");

        this.cache = ignite.getOrCreateCache("myCache");

    }

    public IgniteCache<UUID, Risk> getCache() {
        return cache;
    }

    @Override
    public void send(RiskRunResult riskRunResult) {
        System.out.println("{Risk Result: (" + riskRunResult.getRequest().getType() + "): " + riskRunResult.getId() + " Risk: " + riskRunResult.getRequest() + " fragment: " + riskRunResult.getFragmentNo() + "/" + riskRunResult.getFragmentCount() + "}") ;

        try {
            switch (riskRunResult.getSetKind()) {
                case MR: {
                    MRRunResponse mrrr = (MRRunResponse) riskRunResult;
                    for (Risk r : mrrr.getRisks()) {
                        System.out.println("{RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + "}");
                        if (r.getRiskType() == RiskType.DELTA) {
                            Object oldvalue = this.cache.getAndPut(r.getTcn(), r);
                            if (oldvalue != null) {
                                System.out.println("Replaced existing");
                            }
                        }
                    }
                }
                break;
                case SR: {
                    SRRunResponse srrr = (SRRunResponse) riskRunResult;
                    System.out.println("{RiskType: " + srrr.getRisk().getRiskType() + ", tcn: " + srrr.getRisk().getTcn() + "}");
                    if (srrr.getRisk().getRiskType() == RiskType.DELTA) {
                        Object oldvalue = this.cache.getAndPut(srrr.getRisk().getTcn(), srrr.getRisk());
                        if (oldvalue != null) {
                            System.out.println("Replaced existing");
                        }
                    }
                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
