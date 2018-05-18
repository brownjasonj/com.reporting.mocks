package com.reporting.mocks.persistence;

public interface IPersistenceStoreFactory<T> {
    public T create(String name);
    public T get(String name);
    public T delete(String name);
}
