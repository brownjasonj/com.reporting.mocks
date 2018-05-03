package com.reporting.mocks.model.trade;

import com.reporting.mocks.model.underlying.OtcUnderlying;

public class OtcTrade extends Trade {
    protected OtcUnderlying underlying;

    public OtcTrade(OtcUnderlying underlying, String book) {
        super(TradeKind.Otc, book);
        this.underlying = underlying;
    }

    public OtcTrade(OtcTrade otcTrade) {
        super(otcTrade);
        this.underlying = otcTrade.getUnderlying();
    }

    public OtcUnderlying getUnderlying() {
        return underlying;
    }
}
