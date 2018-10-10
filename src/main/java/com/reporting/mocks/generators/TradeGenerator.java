package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;
import com.reporting.mocks.model.underlying.Underlying;

import java.util.Random;

public class TradeGenerator {

    protected TradeConfig tradeConfig;

    public TradeGenerator(TradeConfig tradeConfig) {
        this.tradeConfig = tradeConfig;
    }

    protected String getBook() {
        return tradeConfig.getBooks().get((new Random()).nextInt(tradeConfig.getBooks().size()));
    }

    protected OtcUnderlying getUnderlying() {
        return tradeConfig.getOtcUnderlying().get((new Random()).nextInt(tradeConfig.getOtcUnderlying().size()));
    }

    protected TradeType getOtcTradeType() {
        return tradeConfig.getTradeTypes().get((new Random()).nextInt(tradeConfig.getTradeTypes().size()));
    }

    public OtcTrade generateOneOtc() {
        return TradeGeneratorFactory.getGenerator(this.getOtcTradeType()).generate(this.getUnderlying(), this.getBook());
    }
}
