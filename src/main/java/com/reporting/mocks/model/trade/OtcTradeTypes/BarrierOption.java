package com.reporting.mocks.model.trade.OtcTradeTypes;

import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

public class BarrierOption extends OtcTrade {
    public BarrierOption(OtcUnderlying underlying, String book) {
        super(TradeType.BarrierOption, underlying, book);
    }

    public BarrierOption(OtcTrade otcTrade) {
        super(otcTrade);
    }

    @Override
    public OtcTrade getNewVersion() {
        return super.getNewVersion();
    }
}
