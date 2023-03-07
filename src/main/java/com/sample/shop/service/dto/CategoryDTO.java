package com.sample.shop.service.dto;

import com.sample.shop.domain.enumeration.CategoryStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * A DTO for the {@link com.sample.shop.domain.Category} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryDTO extends AbstractAuditingDTO<UUID> implements Serializable {

    @KeywordField
    private UUID id;

    @NotNull
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String description;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Integer sortOrder;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateAdded;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateModified;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private CategoryStatus status;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    private CategoryDTO parent;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private UUID parentId;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    private Set<ProductDTO> products = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public CategoryDTO getParent() {
        return parent;
    }

    public void setParent(CategoryDTO parent) {
        this.parent = parent;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
        if (Objects.isNull(parent)) {
            parent = new CategoryDTO();
        }
        parent.setId(parentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id='" + getId() + "'" +
            ", description='" + getDescription() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", status='" + getStatus() + "'" +
            ", parent=" + getParentId() +
            ", products=" + getProducts() +
            "}";
    }
}
