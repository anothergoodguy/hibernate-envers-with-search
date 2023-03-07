package com.sample.shop.service.dto;

import java.io.Serializable;
import java.time.Instant;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

public abstract class AbstractAuditingDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Instant createdDate = Instant.now();

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Instant lastModifiedDate = Instant.now();

    private String createdBy;
    private String lastModifiedBy;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Boolean active = true;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Long cursor = 1L;

    private Long version;

    public abstract T getId();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCursor() {
        return cursor;
    }

    public void setCursor(Long cursor) {
        this.cursor = cursor;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("createdBy", createdBy)
            .append("createdDate", createdDate)
            .append("lastModifiedBy", lastModifiedBy)
            .append("lastModifiedDate", lastModifiedDate)
            .append("active", active)
            .append("cursor", cursor)
            .append("version", version)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractAuditingDTO<?> that = (AbstractAuditingDTO<?>) o;

        return new EqualsBuilder()
            .append(createdBy, that.createdBy)
            .append(createdDate, that.createdDate)
            .append(lastModifiedBy, that.lastModifiedBy)
            .append(lastModifiedDate, that.lastModifiedDate)
            .append(active, that.active)
            .append(cursor, that.cursor)
            .append(version, that.version)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(createdBy)
            .append(createdDate)
            .append(lastModifiedBy)
            .append(lastModifiedDate)
            .append(active)
            .append(cursor)
            .append(version)
            .toHashCode();
    }
}
