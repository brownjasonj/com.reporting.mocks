package com.reporting.mocks.endpoints.ignite;

import com.reporting.mocks.model.risks.Risk;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import java.util.UUID;

public class IgniteListner implements Runnable {
    protected IgniteCache<UUID, Risk> cache;


    public IgniteListner(IgniteCache<UUID, Risk> cache) {
          this.cache = cache;
    }


    @Override
    public void run() {
        // Creating a continuous query.
        ContinuousQuery<UUID, Risk> qry = new ContinuousQuery<>();

        // Local listener that is called locally when an update notification is received.
//        qry.setLocalListener((Iterable<CacheEntryEvent<? extends UUID,? extends Risk>> evts) ->
//                evts.forEach(e -> System.out.println("Cache changed: TCN =" + e.getValue().getTcn())));

        qry.setLocalListener(new CacheEntryUpdatedListener<UUID, Risk>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends UUID, ? extends Risk>> evts) {
                for (CacheEntryEvent<? extends UUID, ? extends Risk> e : evts)
                    System.out.println("Cache changed: TCN =" + e.getValue().getTcn());
            }
        });

        qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<UUID, Risk>>() {
            @Override
            public CacheEntryEventFilter<UUID, Risk> create() {
                return new CacheEntryEventFilter<UUID, Risk>() {
                    @Override
                    public boolean evaluate(CacheEntryEvent<? extends UUID, ? extends Risk> event) throws CacheEntryListenerException {
                        return true;
                    }
                };
            }
        });

        try (QueryCursor<Cache.Entry<UUID, Risk>> cur = cache.query(qry)) {

            for (Cache.Entry<UUID, Risk> e : cur) {
                System.out.println("Cache changed: TCN =" + e.getValue().getTcn());
            }
        }
    }
}
