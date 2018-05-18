package com.reporting.mocks.persistence;

import java.util.Collection;

public interface IPersistenceStore<K, T> {
    public String getName();
    public T add(K k, T t);
    public T get(K k);
    public T oneAtRandom();
    public Collection<T> getAll();
    public Collection<K> getKeys();
    public T delete(K k);
}
