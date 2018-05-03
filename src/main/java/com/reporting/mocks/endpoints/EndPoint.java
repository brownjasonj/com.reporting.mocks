package com.reporting.mocks.endpoints;

public interface EndPoint<T> {
    void Send(T msg);
    T Receive();
}
