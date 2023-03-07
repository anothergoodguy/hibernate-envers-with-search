package com.sample.shop.version.conf.search;

import com.sample.shop.version.domain.identity.CustomEntityId;
import java.io.Serializable;
import java.util.Objects;

public class AuditIdentity<T, R> implements Serializable {

    public static final char SEPARATOR = '_';
    private final EntityId<T> identity;

    private final R revision;

    public AuditIdentity(EntityId<T> identity, R revision) {
        this.identity = identity;
        this.revision = revision;
    }

    public static AuditIdentity parse(String documentIdentifier) {
        int separatorIndex = documentIdentifier.lastIndexOf(SEPARATOR);
        int revId = Integer.parseInt(documentIdentifier.substring(separatorIndex + 1));

        // Note: this is incomplete and needs implementation with generics
        // for now using a static method from the subclass of EntityId
        EntityId entityIdParsed = CustomEntityId.parsed(documentIdentifier.substring(0, separatorIndex));

        return new AuditIdentity(entityIdParsed, revId);
    }

    public String getStringValue() {
        return this.identity.getStringValue() + SEPARATOR + revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditIdentity that = (AuditIdentity) o;
        return identity.equals(that.identity) && revision.equals(that.revision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, revision);
    }
}
