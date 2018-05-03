package com.reporting.mocks.model.underlying;

public class CurrencyPair extends OtcUnderlying {
    protected String ccy1;
    protected String ccy2;

    public CurrencyPair(String ccy1, String ccy2) {
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
    }

    public String getCcy1() {
        return ccy1;
    }

    public String getCcy2() {
        return ccy2;
    }

    @Override
    public String getAccy() {
        return this.ccy2;
    }

    @Override
    public Currency getUnderlyingCurrency() {
        return new Currency(this.ccy2);
    }
}
