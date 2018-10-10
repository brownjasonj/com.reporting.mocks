package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTradeTypes.VanillaOption;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.OtcUnderlying;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class VanillaOptionGenerator implements ITradeGenerator<VanillaOption> {
    @Override
    public TradeType getTradeType() {
        return TradeType.VanillaOption;
    }

    @Override
    public VanillaOption generate(OtcUnderlying underlying, String book) {
        Random rand = new Random();
        Double strike = rand.nextDouble();
        Date expiryDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(2000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        VanillaOption vanillaOption = new VanillaOption(underlying, expiryDate, strike, 1.0, strike, book);
        vanillaOption.setQuantity(new Random().nextDouble() * 1000000);
        vanillaOption.setBuySell(rand.nextBoolean() ? BuySell.Sell : BuySell.Buy);
        return vanillaOption;
    }
}
