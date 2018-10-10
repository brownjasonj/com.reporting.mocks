package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTradeTypes.Spot;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class SpotGenerator implements ITradeGenerator<Spot> {
    @Override
    public TradeType getTradeType() {
        return TradeType.Spot;
    }

    @Override
    public Spot generate(OtcUnderlying underlying, String book) {
        Random rand = new Random();
        Double rate = rand.nextDouble();
        Date settlementDate = Date.from(LocalDate.now().plusDays(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Spot spot = new Spot(underlying, settlementDate, rate, 1.0, rate, book);
        spot.setQuantity(new Random().nextDouble() * 1000000);
        spot.setBuySell(rand.nextBoolean() ? BuySell.Sell : BuySell.Buy);
        return spot;
    }
}
