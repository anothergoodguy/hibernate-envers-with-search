package com.sample.shop.version.domain.identity;

import com.sample.shop.version.conf.search.EntityId;
import java.util.Objects;
import java.util.UUID;

public class CustomEntityId extends EntityId<CustomEntityId> {

    UUID idValue;

    @Override
    public CustomEntityId parse(String partialIdentity) {
        return new CustomEntityId(UUID.fromString(partialIdentity));
    }

    public static CustomEntityId parsed(String partialIdentity) {
        return new CustomEntityId(UUID.fromString(partialIdentity));
    }

    @Override
    public String getStringValue() {
        return idValue.toString();
    }

    public CustomEntityId(UUID idValue) {
        this.idValue = idValue;
    }

    public UUID getIdValue() {
        return idValue;
    }

    public void setIdValue(UUID idValue) {
        this.idValue = idValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEntityId that = (CustomEntityId) o;
        return idValue.equals(that.idValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idValue);
    }
}
