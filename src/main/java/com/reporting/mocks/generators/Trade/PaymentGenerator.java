package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.trade.TradeTypes.Payment;
import com.reporting.mocks.model.underlying.Underlying;

import java.time.*;
import java.util.Date;
import java.util.Random;

public class PaymentGenerator implements ITradeGenerator<Payment> {

    @Override
    public TradeType getTradeType() {
        return TradeType.Payment;
    }

    @Override
    public Payment generate(UnderlyingConfig underlyingConfig, String book) {
        Random rand = new Random();
        Instant settlementDate = Instant.now(Clock.system(ZoneOffset.UTC));
        Underlying underlying1 = underlyingConfig.selectRandomUnderlying1();
        Payment payment = new Payment(
                book,
                rand.nextDouble() * rand.nextInt(1000000),
                underlying1,
                settlementDate);
        return payment;
    }
}
