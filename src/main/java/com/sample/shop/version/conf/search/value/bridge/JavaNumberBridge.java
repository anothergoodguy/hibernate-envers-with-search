package com.sample.shop.version.conf.search.value.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class JavaNumberBridge implements ValueBridge<Number, String> {

    @Override
    public String toIndexedValue(Number value, ValueBridgeToIndexedValueContext context) { // <2>
        return value == null ? null : Integer.toString(value.intValue());
    }

    @Override
    public boolean isCompatibleWith(ValueBridge<?, ?> other) {
        return getClass().equals(other.getClass());
    }
}
