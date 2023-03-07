package com.sample.shop.version.conf.search.value.bridge;

import com.sample.shop.version.conf.search.AuditIdentity;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class AuditValueBridge implements ValueBridge<AuditIdentity, String> { // <1>

    @Override
    public String toIndexedValue(AuditIdentity value, ValueBridgeToIndexedValueContext context) { // <2>
        return value == null ? null : value.getStringValue();
    }

    @Override
    public boolean isCompatibleWith(ValueBridge<?, ?> other) {
        return getClass().equals(other.getClass());
    }
}
