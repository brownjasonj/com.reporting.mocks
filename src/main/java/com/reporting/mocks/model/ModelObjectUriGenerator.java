package com.reporting.mocks.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class ModelObjectUriGenerator {

    public static URI getCalculationContextStoreURI(PricingGroup pricingGroup) {

        try {
            return new URI("/" + pricingGroup.getName());
        }
        catch (URISyntaxException urise) {
            return null;
        }

    }

    public static URI getCalculationContextURI(PricingGroup pricingGroup, UUID id) {

        try {
            return new URI("calculationcontext:/" + pricingGroup.getName() + "?id=" + id);
        }
        catch (URISyntaxException urise) {
            return null;
        }

    }

    public static URI getMarketEnvStoreURI(PricingGroup pricingGroup) {

        try {
            return new URI("/" + pricingGroup.getName());
        }
        catch (URISyntaxException urise) {
            return null;
        }

    }

    public static URI getMarketEnvURI(PricingGroup pricingGroup, DataMarkerType type, UUID id) {

        try {
            return new URI("market:/" + pricingGroup.getName() + "?id=" + id + "&type=" + type);
        }
        catch (URISyntaxException urise) {
            return null;
        }

    }
}
