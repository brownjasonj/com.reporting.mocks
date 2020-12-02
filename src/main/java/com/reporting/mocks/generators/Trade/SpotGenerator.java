package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.trade.TradeTypes.Spot;
import com.reporting.mocks.model.underlying.Underlying;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

public class SpotGenerator implements ITradeGenerator<Spot> {
    @Override
    public TradeType getTradeType() {
        return TradeType.Spot;
    }

    @Override
    public Spot generate(UnderlyingConfig underlyingConfig, String book) {
        Random rand = new Random();
        Double price = rand.nextDouble();
        Instant settlementDate = Clock.system(ZoneOffset.UTC).instant().plus(2, ChronoUnit.DAYS);

        Underlying underlying1 = underlyingConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingConfig.selectRandomUnderlying2(underlying1.getName());
        Spot spot = new Spot(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                settlementDate,
                price);
        return spot;
    }
}
