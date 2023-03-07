package com.sample.shop.version.service.mapper;

import com.sample.shop.domain.Category;
import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductVersionMapper.class })
public interface CategoryVersionMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDTO toDto(Category s);
}
