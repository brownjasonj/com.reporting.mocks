package com.reporting.mocks.configuration;

import com.reporting.mocks.model.underlying.Underlying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UnderlyingConfig {
    protected HashMap<String, List<String>> underlyingCrosses;

    public UnderlyingConfig() {
        this.underlyingCrosses = new HashMap<>();
    }

    public void addSet(String underlying, List<String> underlyingSet) {
        this.underlyingCrosses.put(underlying, underlyingSet);
    }

    public Underlying selectRandomUnderlying1() {
        ArrayList<String> keys = new ArrayList(this.underlyingCrosses.keySet());
        return new Underlying(keys.get(new Random().nextInt(keys.size() - 1)));
    }

    public Underlying selectRandomUnderlying2(String key) {
        if (this.underlyingCrosses.containsKey(key)) {
            List<String> underlyings = this.underlyingCrosses.get(key);
            return new Underlying(underlyings.get(new Random().nextInt(underlyings.size() - 1)));
        }
        else
            return null;
    }

    public HashMap<String, List<String>> getUnderlyingCrosses() {
        return underlyingCrosses;
    }

    public HashMap<String, List<String>> getUnderlyingSets() {
        return underlyingCrosses;
    }
}
