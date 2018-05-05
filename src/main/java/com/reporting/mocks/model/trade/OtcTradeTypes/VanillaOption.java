package com.reporting.mocks.model.trade.OtcTradeTypes;

import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

public class VanillaOption extends OtcTrade {
    public VanillaOption(OtcUnderlying underlying, String book) {
        super(TradeType.VanillaOption, underlying, book);
    }

    public VanillaOption(OtcTrade otcTrade) {
        super(otcTrade);
    }

    @Override
    public OtcTrade getNewVersion() {
        return super.getNewVersion();
    }
}
