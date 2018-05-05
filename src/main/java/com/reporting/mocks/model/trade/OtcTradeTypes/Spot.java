package com.reporting.mocks.model.trade.OtcTradeTypes;

import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.util.Date;
import java.util.Random;

public class Spot extends OtcTrade {
    protected Date settlementDate;
    protected Double rate;
    protected Double amount;

    public Spot(OtcUnderlying underlying, String book) {
        super(TradeType.Spot, underlying, book);
        this.settlementDate = new Date();
        this.rate = (new Random()).nextDouble();
        this.amount = (new Random()).nextDouble();

    }

    public Spot(Spot fxSpot) {
        super(fxSpot);
        this.settlementDate = fxSpot.settlementDate;
        this.rate = fxSpot.rate;
        this.amount = fxSpot.getAmount();
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public Double getRate() {
        return rate;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public OtcUnderlying getUnderlying() {
        return super.getUnderlying();
    }

    @Override
    public Spot getNewVersion() {
        return new Spot(this);
    }
}
