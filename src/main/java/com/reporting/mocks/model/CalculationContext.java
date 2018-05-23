package com.reporting.mocks.model;

import com.reporting.mocks.model.risks.RiskType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

/*
    A CalculationContext represents a set of associations between a risk type and a market environment.
    The association is used to determine which market environment should be used to calculate the specific
    risk.
 */
public class CalculationContext {
    UUID id;
    URI uri;
    Date timeStamp;
    PricingGroup pricingGroup;
    Map<RiskType, MarketEnv> markets;

    public static UUID getIdFromURI(URI uri) {
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        if (parameters.containsKey("id")) {
            List<String> values = parameters.get("id");
            if (values.size() == 1)
                return UUID.fromString(values.get(0));
        }
        return null;
    }

    public CalculationContext(PricingGroup pricingGroup) {
        this.id = UUID.randomUUID();
        this.uri = ModelObjectUriGenerator.getCalculationContextURI(pricingGroup, this.id);
        this.timeStamp = new Date();
        this.pricingGroup = pricingGroup;
        this.markets = new HashMap<>();
    }

    public CalculationContext(CalculationContext calcContext) {
        this(calcContext.pricingGroup);
        this.markets = new HashMap<>(calcContext.markets);
    }

    public void update(List<RiskType> riskTypes, MarketEnv marketEnv) {
        for(RiskType riskType : riskTypes) {
            markets.put(riskType, marketEnv);
        }
    }

    public void add(RiskType riskType, MarketEnv marketEnv) {
        this.markets.put(riskType, marketEnv);
    }

    public MarketEnv get(RiskType riskType) {
        return markets.get(riskType);

    }

    public UUID getId() { return this.id; }
    public URI getUri() {
        return this.uri;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public PricingGroup getPricingGroup() {
        return pricingGroup;
    }

    public Map<RiskType, MarketEnv> getMarkets() {
        return markets;
    }
}
