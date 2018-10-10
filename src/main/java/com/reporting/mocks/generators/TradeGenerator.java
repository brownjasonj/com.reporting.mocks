package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.Random;

public class TradeGenerator {

    protected TradeConfig tradeConfig;

    public TradeGenerator(TradeConfig tradeConfig) {
        this.tradeConfig = tradeConfig;
    }

    protected String getBook() {
        return tradeConfig.getBooks().get((new Random()).nextInt(tradeConfig.getBooks().size()));
    }

    protected TradeType getOtcTradeType() {
        return tradeConfig.getTradeTypes().get((new Random()).nextInt(tradeConfig.getTradeTypes().size()));
    }

    public Trade generateOneOtc() {
        return TradeGeneratorFactory.getGenerator(this.getOtcTradeType()).generate(tradeConfig.getUnderlyings(), this.getBook());
    }
}
