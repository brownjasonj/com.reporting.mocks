package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTradeTypes.BarrierOption;
import com.reporting.mocks.model.trade.OtcTradeTypes.VanillaOption;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class BarrierOptionGenerator implements ITradeGenerator<BarrierOption> {
    @Override
    public TradeType getTradeType() {
        return TradeType.BarrierOption;
    }

    @Override
    public BarrierOption generate(OtcUnderlying underlying, String book) {
        Random rand = new Random();
        Double strike = rand.nextDouble();
        Double barrier = strike + rand.nextDouble();
        Date expiryDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(2000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        BarrierOption barrierOption = new BarrierOption(underlying, expiryDate, strike, barrier, 1.0, strike, book);
        barrierOption.setQuantity(new Random().nextDouble() * 1000000);
        barrierOption.setBuySell(rand.nextBoolean() ? BuySell.Sell : BuySell.Buy);
        return barrierOption;
    }
}
