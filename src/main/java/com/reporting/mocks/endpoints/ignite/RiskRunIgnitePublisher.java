package com.reporting.mocks.endpoints.ignite;

import com.reporting.mocks.endpoints.RiskRunPublisher;
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
import java.util.UUID;

public class RiskRunIgnitePublisher implements RiskRunPublisher {
    protected Ignite ignite;
    protected IgniteCache<String, TcnRiskAggregate> bookAggregate;
    protected IgniteCache<UUID, TcnRisksAll> bookTcnRisksAll;


    public RiskRunIgnitePublisher() {
        Ignition.setClientMode(true);
        this.ignite = Ignition.start("examples/config/example-ignite.xml");

//        this.cache = ignite.getOrCreateCache("myCache");

    }

    @Override
    public void send(RiskRunResult riskRunResult) {
        System.out.println("{Risk Result: (" + riskRunResult.getRequest().getType() + "): " + riskRunResult.getId() + " Risk: " + riskRunResult.getRequest() + " fragment: " + riskRunResult.getFragmentNo() + "/" + riskRunResult.getFragmentCount() + "}") ;

        String bookRiskTcnAggregate = riskRunResult.getRequest().getMarketEnvId().toString() + "/Book/Risks/Tcn/Aggregate";
        String bookTcnRisksAll = riskRunResult.getRequest().getMarketEnvId().toString() + "/Book/Tcn/Risks/All";

        this.bookAggregate = ignite.getOrCreateCache(bookRiskTcnAggregate);
        this.bookTcnRisksAll = ignite.getOrCreateCache(bookTcnRisksAll);

        try {
            switch (riskRunResult.getSetKind()) {
                case MR: {
                    MRRunResponse mrrr = (MRRunResponse) riskRunResult;
                    for (Risk r : mrrr.getRisks()) {
                        System.out.println("{Cache: " + bookTcnRisksAll + ", RiskType: " + r.getRiskType() + ", tcn: " + r.getTcn() + "}");
                        UUID tcn = r.getTcn();
                        TcnRisksAll allRisks = this.bookTcnRisksAll.get(tcn);
                        if (allRisks == null) {
                            allRisks = new TcnRisksAll(tcn);
                        }
                        allRisks.setRisk(r);
                        this.bookTcnRisksAll.getAndPut(tcn, allRisks);
                    }
                }
                break;
                case SR: {
                    SRRunResponse srrr = (SRRunResponse) riskRunResult;
                    System.out.println("{Cache: " + bookTcnRisksAll + ", RiskType: " + srrr.getRisk().getRiskType() + ", tcn: " + srrr.getRisk().getTcn() + "}");
                    Risk r = srrr.getRisk();
                    UUID tcn = r.getTcn();
                    TcnRisksAll allRisks = this.bookTcnRisksAll.get(tcn);
                    if (allRisks == null) {
                        allRisks = new TcnRisksAll(tcn);
                    }
                    allRisks.setRisk(r);
                    this.bookTcnRisksAll.getAndPut(tcn, allRisks);
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
