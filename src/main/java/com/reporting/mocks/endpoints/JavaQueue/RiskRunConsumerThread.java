package com.reporting.mocks.endpoints.JavaQueue;

import com.google.gson.Gson;
import com.reporting.mocks.model.RiskResultSet;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RiskRunConsumerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( RiskRunConsumerThread.class.getName() );
    protected BlockingQueue<RiskResultSet> riskResultSetQueue;

    public RiskRunConsumerThread(BlockingQueue<RiskResultSet> riskResultSetQueue) {
        this.riskResultSetQueue = riskResultSetQueue;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        while(true) {
            try {
                RiskResultSet result = this.riskResultSetQueue.take();

                String resultString = gson.toJson(result);

                System.out.println(resultString);
                //System.out.println("{Risk Result: (" + result.getRequest().getType() + "): " + result.getId() + " Risk: " + result.getRequest() + " fragment: " + result.getFragmentNo() + "/" + result.getFragmentCount() + "}") ;
            }
            catch (InterruptedException e) {
                LOGGER.log( Level.FINE, "thread interrupted");
            }
        }
    }
}
