package com.reporting.mocks.generators;

import com.reporting.mocks.configuration.TradeConfig;
import com.reporting.mocks.model.trade.BuySell;
import com.reporting.mocks.model.trade.OtcTrade;
import com.reporting.mocks.model.trade.TradeType;
import com.reporting.mocks.model.trade.OtcTradeTypes.*;
import com.reporting.mocks.model.underlying.OtcUnderlying;
import com.reporting.mocks.model.underlying.Underlying;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TradeGenerator {
    protected static HashMap<TradeType, Class<? extends OtcTrade>> otcTradeClasses;

    protected static void registerOtcTrade(TradeType type, Class<? extends OtcTrade> classObject) {
        TradeGenerator.otcTradeClasses.put(type, classObject);
    }

    protected static OtcTrade getOtcTrade(TradeType type, Underlying underlying, String book) {
        Class<? extends OtcTrade> otcClass = otcTradeClasses.get(type);

        try {
            Constructor constructor = otcClass.getConstructor(new Class[]{OtcUnderlying.class, String.class});
            OtcTrade otcTrade = (OtcTrade) constructor.newInstance(new Object[]{underlying, book});
            otcTrade.setQuantity(new Random().nextDouble() * 1000000);
            if (new Random().nextBoolean())
                otcTrade.setBuySell(BuySell.Sell);
            else
                otcTrade.setBuySell(BuySell.Buy);
            return otcTrade;
        }
        catch (Exception e) {
            return null;
        }
    }

    static {
        TradeGenerator.otcTradeClasses = new HashMap<>();
        TradeGenerator.registerOtcTrade(TradeType.Spot, Spot.class);
        TradeGenerator.registerOtcTrade(TradeType.Forward, Forward.class);
        TradeGenerator.registerOtcTrade(TradeType.Swap, Swap.class);
        TradeGenerator.registerOtcTrade(TradeType.VanillaOption, VanillaOption.class);
        TradeGenerator.registerOtcTrade(TradeType.BarrierOption, BarrierOption.class);
    }


    protected TradeConfig tradeConfig;

    public TradeGenerator(TradeConfig tradeConfig) {
        this.tradeConfig = tradeConfig;
    }

    protected String getBook() {
        return tradeConfig.getBooks().get((new Random()).nextInt(tradeConfig.getBooks().size()));
    }

    protected Underlying getUnderlying() {
        return tradeConfig.getOtcUnderlying().get((new Random()).nextInt(tradeConfig.getOtcUnderlying().size()));
    }

    protected TradeType getOtcTradeType() {
        return tradeConfig.getTradeTypes().get((new Random()).nextInt(tradeConfig.getTradeTypes().size()));
    }

    public OtcTrade generateOneOtc() {
        return TradeGenerator.getOtcTrade(this.getOtcTradeType(), this.getUnderlying(), this.getBook());
    }

    public List<OtcTrade> generateSetOtc(int count) {
        ArrayList<OtcTrade> trades = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            trades.add(this.generateOneOtc());
        }
        return trades;
    }
}
