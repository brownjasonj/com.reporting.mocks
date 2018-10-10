package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTradeTypes.Swap;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class SwapGenerator implements ITradeGenerator<Swap> {
    @Override
    public TradeType getTradeType() {
        return TradeType.Swap;
    }

    @Override
    public Swap generate(OtcUnderlying underlying, String book) {
        Random rand = new Random();
        Double rate = rand.nextDouble();
        Date nearSettlementDate = Date.from(LocalDate.now().plusDays(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date farSettlementDate = Date.from(LocalDate.now().plusDays(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Swap swap = new Swap(underlying, nearSettlementDate, farSettlementDate, rate, 1.0, rate, book);
        swap.setQuantity(new Random().nextDouble() * 1000000);
        swap.setBuySell(rand.nextBoolean() ? BuySell.Sell : BuySell.Buy);
        return swap;
    }
}
