package com.sample.shop.version.domain;

import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.version.conf.search.AuditIdentity;
import com.sample.shop.version.conf.search.value.binder.AuditIdentityBinder;
import com.sample.shop.version.conf.search.value.binder.AuditValueBinder;
import com.sample.shop.version.conf.search.value.binder.JavaNumberValueBinder;
import com.sample.shop.version.conf.search.value.converter.AuditAttributeConverter;
import com.sample.shop.version.conf.search.value.converter.JavaNumberConverter;
import com.sample.shop.version.domain.identity.CustomEntityId;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.envers.RevisionType;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.IdentifierBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Entity
@Indexed
public class CategoryVersion {

    @Id
    @DocumentId(identifierBinder = @IdentifierBinderRef(type = AuditIdentityBinder.class))
    @Convert(converter = AuditAttributeConverter.class)
    @KeywordField(
        // valueBridge = @ValueBridgeRef(type = AuditValueBridge.class),
        valueBinder = @ValueBinderRef(type = AuditValueBinder.class)
    )
    private AuditIdentity mEntityId;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    private CategoryDTO categoryDTO;

    @Convert(converter = JavaNumberConverter.class)
    @GenericField(
        projectable = Projectable.YES,
        searchable = Searchable.YES,
        // valueBridge = @ValueBridgeRef(type = JavaNumberBridge.class),
        valueBinder = @ValueBinderRef(type = JavaNumberValueBinder.class)
    )
    private Number revision;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private RevisionType revisionType;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
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

    public CategoryVersion(Number revision, RevisionType revisionType, UUID revisedBy, String revisedByType, String auditor) {
        this.categoryDTO = null;
        this.revision = revision;
        this.revisionType = revisionType;
        this.revisedBy = revisedBy;
        this.revisedByType = revisedByType;
        this.auditor = auditor;
        this.mEntityId = new AuditIdentity(new CustomEntityId(categoryDTO.getId()), revision.intValue());
    }

    public CategoryVersion(
        CategoryDTO categoryDTO,
        Number revision,
        RevisionType revisionType,
        UUID revisedBy,
        String revisedByType,
        String auditor
    ) {
        this.categoryDTO = categoryDTO;
        this.revision = revision;
        this.revisionType = revisionType;
        this.revisedBy = revisedBy;
        this.revisedByType = revisedByType;
        this.auditor = auditor;
        this.mEntityId = new AuditIdentity(new CustomEntityId(categoryDTO.getId()), revision.intValue());
    }

    public CategoryVersion() {}

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public Number getRevision() {
        return revision;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public UUID getRevisedBy() {
        return revisedBy;
    }

    public String getRevisedByType() {
        return revisedByType;
    }

    public String getAuditor() {
        return auditor;
    }

    public AuditIdentity getEntityId() {
        return mEntityId;
    }

    public void setEntityId(AuditIdentity entityId) {
        mEntityId = entityId;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public void setRevision(Number revision) {
        this.revision = revision;
    }

    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public void setRevisedBy(UUID revisedBy) {
        this.revisedBy = revisedBy;
    }

    public void setRevisedByType(String revisedByType) {
        this.revisedByType = revisedByType;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryVersion that = (CategoryVersion) o;
        return (
            Objects.equals(mEntityId, that.mEntityId) &&
            categoryDTO.equals(that.categoryDTO) &&
            revision.equals(that.revision) &&
            revisionType == that.revisionType &&
            revisedBy.equals(that.revisedBy) &&
            revisedByType.equals(that.revisedByType) &&
            auditor.equals(that.auditor)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(mEntityId, categoryDTO, revision, revisionType, revisedBy, revisedByType, auditor);
    }

    @Override
    public String toString() {
        return (
            "CategoryVersion{" +
            "mEntityId=" +
            mEntityId +
            ", categoryDTO=" +
            categoryDTO +
            ", revision=" +
            revision +
            ", revisionType=" +
            revisionType +
            ", revisedBy=" +
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
