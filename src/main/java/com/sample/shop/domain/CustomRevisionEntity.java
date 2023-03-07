package com.sample.shop.domain;

import com.sample.shop.config.audit.CustomRevisionListener;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "audit_event_data")
@Indexed(index = "revision_entity")
public class CustomRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @GenericField
    private int id;

    @RevisionTimestamp
    @GenericField
    private long timestamp;

    /*
    @GenericField
    private long refRevisionId;
*/

    @GenericField
    private UUID revisedBy;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String revisedByType;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String auditor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    /*
    public long getRefRevisionId() {
        return refRevisionId;
    }

    public void setRefRevisionId(long refRevisionId) {
        this.refRevisionId = refRevisionId;
    }
*/

    public UUID getRevisedBy() {
        return revisedBy;
    }

    public void setRevisedBy(UUID revisedBy) {
        this.revisedBy = revisedBy;
    }

    public String getRevisedByType() {
        return revisedByType;
    }

    public void setRevisedByType(String revisedByType) {
        this.revisedByType = revisedByType;
    }

    @Transient
    public Date getRevisionDate() {
        return new Date(timestamp);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CustomRevisionEntity that = (CustomRevisionEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(timestamp, that.timestamp)
            //.append(refRevisionId, that.refRevisionId)
            .append(revisedBy, that.revisedBy)
            .append(revisedByType, that.revisedByType)
            .append(auditor, that.auditor)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(timestamp)
            //.append(refRevisionId)
            .append(revisedBy)
            .append(revisedByType)
            .append(auditor)
            .toHashCode();
    }

    @Override
    public String toString() {
        return (
            "CustomRevisionEntity{" +
            "id=" +
            id +
            ", timestamp=" +
            timestamp +
            ", refRevisionId=" +
            /*
            refRevisionId +
            ", revisedBy=" +
*/
            revisedBy +
            ", revisedByType='" +
            revisedByType +
            '\'' +
            ", auditor='" +
            auditor +
            '\'' +
            '}'
        );
    }
}
