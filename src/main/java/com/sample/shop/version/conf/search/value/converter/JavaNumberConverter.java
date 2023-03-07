package com.sample.shop.version.conf.search.value.converter;

import javax.persistence.AttributeConverter;

public class JavaNumberConverter implements AttributeConverter<Number, String> {

    @Override
    public String convertToDatabaseColumn(Number attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public Number convertToEntityAttribute(String dbData) {
        return dbData == null ? null : (Number) Long.parseLong(dbData);
    }
}
