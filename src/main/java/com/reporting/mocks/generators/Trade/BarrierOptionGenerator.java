package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingSetConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeTypes.BarrierOption;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.Underlying;

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
    public BarrierOption generate(UnderlyingSetConfig underlyingSetConfig, String book) {
        Random rand = new Random();
        Double strike = rand.nextDouble();
        Double barrier = strike + rand.nextDouble();
        Date expiryDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(2000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Underlying underlying1 = underlyingSetConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingSetConfig.selectRandomUnderlying(underlying1.getName());
        BarrierOption barrierOption = new BarrierOption(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                expiryDate, strike, barrier);
        return barrierOption;
    }
}
