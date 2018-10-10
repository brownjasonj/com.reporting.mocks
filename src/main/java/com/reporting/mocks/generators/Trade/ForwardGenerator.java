package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingSetConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.trade.TradeTypes.Forward;
import com.reporting.mocks.model.underlying.Underlying;

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
    public Forward generate(UnderlyingSetConfig underlyingSetConfig, String book) {
        Random rand = new Random();
        Double rate = rand.nextDouble();
        Date settlementDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(1000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Underlying underlying1 = underlyingSetConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingSetConfig.selectRandomUnderlying(underlying1.getName());
        Forward forward = new Forward(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                settlementDate, rate);
        return forward;
    }
}
