package com.reporting.mocks.generators;

import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

public interface ITradeGenerator<T extends OtcTrade> {
    TradeType getTradeType();
    T generate(OtcUnderlying underlying, String book);
}
