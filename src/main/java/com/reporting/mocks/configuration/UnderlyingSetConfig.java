package com.reporting.mocks.configuration;

import com.reporting.mocks.model.underlying.Underlying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UnderlyingSetConfig {
    protected HashMap<String, List<String>> underlyingSets;

    public UnderlyingSetConfig() {
        this.underlyingSets = new HashMap<>();
    }

    public void addSet(String underlying, List<String> underlyingSet) {
        this.underlyingSets.put(underlying, underlyingSet);
    }

    public Underlying selectRandomUnderlying1() {
        ArrayList<String> keys = new ArrayList(this.underlyingSets.keySet());
        return new Underlying(keys.get(new Random().nextInt(keys.size() - 1)));
    }

    public Underlying selectRandomUnderlying(String key) {
        if (this.underlyingSets.containsKey(key)) {
            List<String> underlyings = this.underlyingSets.get(key);
            return new Underlying(underlyings.get(new Random().nextInt(underlyings.size() - 1)));
        }
        else
            return null;
    }

    public HashMap<String, List<String>> getUnderlyingSets() {
        return underlyingSets;
    }
}
