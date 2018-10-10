package com.reporting.mocks.generators.Trade;

import com.reporting.mocks.configuration.UnderlyingConfig;
import com.reporting.mocks.generators.ITradeGenerator;
import com.reporting.mocks.model.trade.TradeTypes.VanillaOption;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.underlying.Underlying;

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
    public VanillaOption generate(UnderlyingConfig underlyingConfig, String book) {
        Random rand = new Random();
        Double strike = rand.nextDouble();
        Date expiryDate = Date.from(LocalDate.now().plusDays(2 + rand.nextInt(2000)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Underlying underlying1 = underlyingConfig.selectRandomUnderlying1();
        Underlying underlying2 = underlyingConfig.selectRandomUnderlying2(underlying1.getName());
        VanillaOption vanillaOption = new VanillaOption(
                book,
                new Random().nextDouble() * 1000000,
                underlying1,
                underlying2,
                expiryDate, strike);
        return vanillaOption;
    }
}
