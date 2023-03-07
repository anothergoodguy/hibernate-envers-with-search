package com.sample.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
 * A WishList.
 */
@Entity
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditingEntity.class) })
@Table(name = "wish_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "wishlist-read", createIndex = false)
@Indexed(index = "wishlist")
@SQLDelete(
    sql = "UPDATE wish_list SET active = false, last_modified_date =  CURRENT_TIMESTAMP() WHERE id = ?",
    check = ResultCheckStyle.COUNT
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishList extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    @Field(type = FieldType.Keyword)
    @KeywordField
    private UUID id;

    @NotNull
    @Column(name = "title", nullable = false)
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String title;

    @Column(name = "restricted")
    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Boolean restricted;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "wishList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "wishList", "categories" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wishLists", "addresses" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    public UUID getId() {
        return this.id;
    }

    public WishList id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public WishList title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRestricted() {
        return this.restricted;
    }

    public WishList restricted(Boolean restricted) {
        this.setRestricted(restricted);
        return this;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setWishList(null));
        }
        if (products != null) {
            products.forEach(i -> i.setWishList(this));
        }
        this.products = products;
    }

    public WishList products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public WishList addProduct(Product product) {
        this.products.add(product);
        product.setWishList(this);
        return this;
    }

    public WishList removeProduct(Product product) {
        this.products.remove(product);
        product.setWishList(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WishList customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishList)) {
            return false;
        }
        return id != null && id.equals(((WishList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishList{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", restricted='" + getRestricted() + "'" +
            "}";
    }
}
