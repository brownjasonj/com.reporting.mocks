package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.trade.TradeTypes.Forward;
import com.reporting.mocks.model.underlying.Underlying;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

public class ForwardGenerator implements ITradeGenerator<Forward> {
    @Override
    public TradeType getTradeType() {
        return TradeType.Forward;
    }

    @Override
    public Forward generate(UnderlyingConfig underlyingConfig, String book) {
        Random rand = new Random();
        Double rate = rand.nextDouble();
        Instant settlementDate = Clock.system(ZoneOffset.UTC).instant().plus(2 + rand.nextInt(1000), ChronoUnit.DAYS);
        Underlying underlying1 = underlyingConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingConfig.selectRandomUnderlying2(underlying1.getName());
        Forward forward = new Forward(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                settlementDate, rate);
        return forward;
    }
}
