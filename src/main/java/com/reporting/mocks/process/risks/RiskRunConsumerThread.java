package com.reporting.mocks.process.risks;

import com.reporting.mocks.endpoints.kafka.MRRiskResultKafkaProducer;
import com.reporting.mocks.process.risks.response.RiskRunResult;

import java.util.concurrent.BlockingQueue;

public class RiskRunConsumerThread implements Runnable {
    protected BlockingQueue<RiskRunResult> riskResultQueue;
    protected MRRiskResultKafkaProducer riskResultProducer;

    public RiskRunConsumerThread(BlockingQueue<RiskRunResult> riskResultQueue) {
        this.riskResultQueue = riskResultQueue;
        this.riskResultProducer = new MRRiskResultKafkaProducer();
    }

    @Override
    public void run() {

        while(true) {
            try {
                RiskRunResult result = this.riskResultQueue.take();

                System.out.println("{Risk Result: (" + result.getRequest().getType() + "): " + result.getId() + " Risk: " + result.getRequest() + " fragment: " + result.getFragmentNo() + "/" + result.getFragmentCount() + "}") ;

                switch (result.getSetKind()) {
                    case MR: {
                        System.out.print("Sending to Kafka...");
                        this.riskResultProducer.sendMessage(result);
                        System.out.println("Sent");
                    }
                    break;
                }

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
