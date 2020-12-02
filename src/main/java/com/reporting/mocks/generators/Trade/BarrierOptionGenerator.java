package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeTypes.BarrierOption;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.Underlying;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

public class BarrierOptionGenerator implements ITradeGenerator<BarrierOption> {
    @Override
    public TradeType getTradeType() {
        return TradeType.BarrierOption;
    }

    @Override
    public BarrierOption generate(UnderlyingConfig underlyingConfig, String book) {
        Random rand = new Random();
        Double strike = rand.nextDouble();
        Double barrier = strike + rand.nextDouble();
        Instant expiryDate = Clock.system(ZoneOffset.UTC).instant().plus(2 + rand.nextInt(2000), ChronoUnit.DAYS);
        Underlying underlying1 = underlyingConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingConfig.selectRandomUnderlying2(underlying1.getName());
        BarrierOption barrierOption = new BarrierOption(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                expiryDate, strike, barrier);
        return barrierOption;
    }
}
