package com.sample.shop.version.conf.search;

import java.io.Serializable;
import org.hibernate.annotations.Immutable;

public abstract class EntityId<T> implements Serializable {

    public abstract String getStringValue();

    public abstract T parse(String partialIdentity);
}
