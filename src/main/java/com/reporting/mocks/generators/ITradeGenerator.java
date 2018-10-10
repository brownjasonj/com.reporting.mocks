package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

public interface ITradeGenerator<T extends Trade> {
    TradeType getTradeType();
    T generate(UnderlyingConfig underlyings, String book);
}
