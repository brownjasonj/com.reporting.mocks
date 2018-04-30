package com.reporting.mocks.process.intraday;

public class IntradayEvent<T> {
    protected IntradayEventType type;
    protected T event;

    public IntradayEvent(IntradayEventType type, T event) {
        this.type = type;
        this.event = event;
    }

    public IntradayEventType getType() {
        return type;
    }

    public T getEvent() {
        return event;
    }
}
