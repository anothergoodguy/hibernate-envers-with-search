package com.sample.shop.service.dto;

import java.io.Serializable;
import java.util.Objects;
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
 * A DTO for the {@link com.sample.shop.domain.WishList} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishListDTO extends AbstractAuditingDTO<UUID> implements Serializable {

    @KeywordField
    private UUID id;

    @NotNull
    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String title;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Boolean restricted;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    private CustomerDTO customer;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private UUID customerId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
        if (Objects.isNull(customer)) {
            customer = new CustomerDTO();
        }
        customer.setId(customerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishListDTO)) {
            return false;
        }

        WishListDTO wishListDTO = (WishListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wishListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishListDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", restricted='" + getRestricted() + "'" +
            ", customer=" + getCustomerId() +
            "}";
    }
}
