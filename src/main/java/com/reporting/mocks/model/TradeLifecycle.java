package com.reporting.mocks.model;

import com.reporting.mocks.model.trade.Trade;

public class TradeLifecycle {
    protected Trade trade;
    protected TradeLifecycleType lifecycleType;

    public TradeLifecycle(TradeLifecycleType type, Trade trade) {
        this.trade = trade;
        this.lifecycleType = type;
    }

    public Trade getTrade() {
        return trade;
    }

    public TradeLifecycleType getLifecycleType() {
        return lifecycleType;
    }
}
