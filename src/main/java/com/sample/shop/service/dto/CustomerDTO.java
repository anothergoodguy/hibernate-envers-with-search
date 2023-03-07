package com.sample.shop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * A DTO for the {@link com.sample.shop.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO extends AbstractAuditingDTO<UUID> implements Serializable {

    @KeywordField
    private UUID id;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String firstName;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String lastName;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String email;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String telephone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
