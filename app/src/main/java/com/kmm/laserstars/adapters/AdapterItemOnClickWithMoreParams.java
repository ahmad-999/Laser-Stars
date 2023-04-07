package com.kmm.laserstars.adapters;

public interface AdapterItemOnClickWithMoreParams<T> {
    void onClick(T object,int id,int... pos);
}
