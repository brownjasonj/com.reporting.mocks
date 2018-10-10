package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTradeTypes.Forward;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class ForwardGenerator implements ITradeGenerator<Forward> {
    @Override
    public TradeType getTradeType() {
        return TradeType.Forward;
    }

    @Override
    public Forward generate(OtcUnderlying underlying, String book) {
        Random rand = new Random();
        Double rate = rand.nextDouble();
        Date settlementDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(1000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Forward forward = new Forward(underlying, settlementDate, rate, 1.0, rate, book);
        forward.setQuantity(new Random().nextDouble() * 1000000);
        forward.setBuySell(rand.nextBoolean() ? BuySell.Sell : BuySell.Buy);
        return forward;
    }
}
