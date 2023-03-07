package com.sample.shop.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
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
 * A DTO for the {@link com.sample.shop.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO extends AbstractAuditingDTO<UUID> implements Serializable {

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

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String keywords;

    @FullTextField(
        analyzer = "autocomplete_indexing",
        searchAnalyzer = "autocomplete_search",
        projectable = Projectable.YES,
        searchable = Searchable.YES
    )
    private String description;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private Integer rating;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateAdded;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private LocalDate dateModified;

    @IndexedEmbedded(includeDepth = 1, structure = ObjectStructure.NESTED)
    private WishListDTO wishList;

    @GenericField(projectable = Projectable.YES, searchable = Searchable.YES)
    private UUID wishListId;

    //private Set<CategoryDTO> categories = new HashSet<CategoryDTO>();
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public WishListDTO getWishList() {
        return wishList;
    }

    public void setWishList(WishListDTO wishList) {
        this.wishList = wishList;
    }

    public UUID getWishListId() {
        return wishListId;
    }

    public void setWishListId(UUID wishListId) {
        this.wishListId = wishListId;
        if (Objects.isNull(wishList)) {
            wishList = new WishListDTO();
        }
        wishList.setId(wishListId);
    }

    /*public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", description='" + getDescription() + "'" +
            ", rating=" + getRating() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", wishList=" + getWishListId() +
            "}";
    }
}
