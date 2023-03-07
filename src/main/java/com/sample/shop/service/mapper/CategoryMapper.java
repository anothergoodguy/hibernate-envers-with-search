package com.sample.shop.service.mapper;

import com.sample.shop.domain.Category;
import com.sample.shop.repository.CategoryRepository;
import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public abstract class CategoryMapper implements EntityMapper<CategoryDTO, Category> {

    @Autowired
    CategoryRepository mRepository;

    @Mapping(target = "parentId", source = "parent.id")
    public abstract CategoryDTO toDto(Category s);

    @Mapping(target = "parent", source = "parentId")
    @Mapping(target = "removeProduct", ignore = true)
    public abstract Category toEntity(CategoryDTO s);

    public Category fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Category entry = mRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", getClass().getName(), "Entry id is null"));
        return entry;
    }
}
