package com.reporting.mocks.model.trade;

import com.reporting.mocks.model.underlying.OtcUnderlying;

public class OtcTrade extends Trade {
    protected OtcUnderlying underlying;

    public OtcTrade(TradeType tradeType, OtcUnderlying underlying, String book) {
        super(TradeKind.Otc, tradeType, book);
        this.underlying = underlying;
    }

    public OtcTrade(OtcTrade otcTrade) {
        super(otcTrade);
        this.underlying = otcTrade.getUnderlying();
    }

    public OtcUnderlying getUnderlying() {
        return underlying;
    }

    @Override
    public OtcTrade getNewVersion() {
        return new OtcTrade(this);
    }

    @Override
    public String toString() {
        return "{Type: " + this.tradeType + ", Book: " + book + ", TCN: " + tcn + "/" + version + "}";
    }
}
