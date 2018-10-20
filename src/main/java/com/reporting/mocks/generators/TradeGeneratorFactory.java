package com.reporting.mocks.generators;

import com.reporting.mocks.generators.Trade.*;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.HashMap;

public class TradeGeneratorFactory {
    private static HashMap<TradeType, ITradeGenerator<? extends Trade>> generators = new HashMap<>();

    static {
        TradeGeneratorFactory.register(new PaymentGenerator());
        TradeGeneratorFactory.register(new SpotGenerator());
        TradeGeneratorFactory.register(new SwapGenerator());
        TradeGeneratorFactory.register(new ForwardGenerator());
        TradeGeneratorFactory.register(new VanillaOptionGenerator());
        TradeGeneratorFactory.register(new BarrierOptionGenerator());
    }

    private static void register(ITradeGenerator<? extends Trade> tradeGenerator) {
        TradeGeneratorFactory.generators.put(tradeGenerator.getTradeType(), tradeGenerator);
    }

    public static ITradeGenerator<? extends Trade> getGenerator(TradeType tradeType) {
        if (TradeGeneratorFactory.generators.containsKey(tradeType)) {
            return TradeGeneratorFactory.generators.get(tradeType);
        }
        else {
            return null;
        }
    }
}
