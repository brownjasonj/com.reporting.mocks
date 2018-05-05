package com.reporting.mocks.model.trade.OtcTradeTypes;

import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

public class Swap extends OtcTrade{
    public Swap(OtcUnderlying underlying, String book) {
        super(TradeType.Forward, underlying, book);
    }

    public Swap(OtcTrade otcTrade) {
        super(otcTrade);
    }

    @Override
    public OtcTrade getNewVersion() {
        return super.getNewVersion();
    }
}
