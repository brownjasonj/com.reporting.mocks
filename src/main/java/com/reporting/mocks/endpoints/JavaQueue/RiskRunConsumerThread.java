package com.reporting.mocks.endpoints.JavaQueue;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.process.trades.TradePopulationProducerThread;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RiskRunConsumerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( RiskRunConsumerThread.class.getName() );
    protected BlockingQueue<RiskResult> riskResultQueue;

    public RiskRunConsumerThread(BlockingQueue<RiskResult> riskResultQueue) {
        this.riskResultQueue = riskResultQueue;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        while(true) {
            try {
                RiskResult result = this.riskResultQueue.take();

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
