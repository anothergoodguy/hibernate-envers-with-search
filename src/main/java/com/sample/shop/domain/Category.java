package com.sample.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.shop.domain.enumeration.CategoryStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Category.
 */
@Entity
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditingEntity.class) })
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "category-read", createIndex = false)
@Indexed(index = "category")
@SQLDelete(
    sql = "UPDATE category SET active = false, last_modified_date =  CURRENT_TIMESTAMP() WHERE id = ?",
    check = ResultCheckStyle.COUNT
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    @Field(type = FieldType.Keyword)
    @KeywordField
    private UUID id;

    @NotNull
    @Column(name = "description", nullable = false)
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String description;

    @Column(name = "sort_order")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Integer sortOrder;

    @Column(name = "date_added")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateModified;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private CategoryStatus status;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "products" }, allowSetters = true)
    private Category parent;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_category__product",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "wishList", "categories" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    public UUID getId() {
        return this.id;
    }

    public Category id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Category sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Category dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Category dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public CategoryStatus getStatus() {
        return this.status;
    }

    public Category status(CategoryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public Category getParent() {
        return this.parent;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Category products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Category addProduct(Product product) {
        this.products.add(product);
        product.getCategories().add(this);
        return this;
    }

    public Category removeProduct(Product product) {
        this.products.remove(product);
        product.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
