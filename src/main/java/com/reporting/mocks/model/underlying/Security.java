package com.reporting.mocks.model.underlying;

public class Security extends Underlying {
    protected String securityId;
    protected String ccy1;


    public String getSecurityId() {
        return securityId;
    }

    public String getCcy1() {
        return ccy1;
    }

    @Override
    public Currency getUnderlyingCurrency() {
        return new Currency(this.ccy1);
    }
}
