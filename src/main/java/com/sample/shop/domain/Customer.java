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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Customer.
 */
@Entity
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditingEntity.class) })
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customer-read", createIndex = false)
@Indexed(index = "customer")
@SQLDelete(
    sql = "UPDATE customer SET active = false, last_modified_date =  CURRENT_TIMESTAMP() WHERE id = ?",
    check = ResultCheckStyle.COUNT
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    @Field(type = FieldType.Keyword)
    @KeywordField
    private UUID id;

    @Column(name = "first_name")
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String firstName;

    @Column(name = "last_name")
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String lastName;

    @Column(name = "email")
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String email;

    @Column(name = "telephone")
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String telephone;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products", "customer" }, allowSetters = true)
    private Set<WishList> wishLists = new HashSet<>();

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    public UUID getId() {
        return this.id;
    }

    public Customer id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Customer telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<WishList> getWishLists() {
        return this.wishLists;
    }

    public void setWishLists(Set<WishList> wishLists) {
        if (this.wishLists != null) {
            this.wishLists.forEach(i -> i.setCustomer(null));
        }
        if (wishLists != null) {
            wishLists.forEach(i -> i.setCustomer(this));
        }
        this.wishLists = wishLists;
    }

    public Customer wishLists(Set<WishList> wishLists) {
        this.setWishLists(wishLists);
        return this;
    }

    public Customer addWishList(WishList wishList) {
        this.wishLists.add(wishList);
        wishList.setCustomer(this);
        return this;
    }

    public Customer removeWishList(WishList wishList) {
        this.wishLists.remove(wishList);
        wishList.setCustomer(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomer(this));
        }
        this.addresses = addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
