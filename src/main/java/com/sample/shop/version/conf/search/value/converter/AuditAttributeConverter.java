package com.sample.shop.version.conf.search.value.converter;

import com.sample.shop.version.conf.search.AuditIdentity;
import javax.persistence.AttributeConverter;

public class AuditAttributeConverter implements AttributeConverter<AuditIdentity, String> {

    @Override
    public String convertToDatabaseColumn(AuditIdentity attribute) {
        return attribute == null ? null : attribute.getStringValue();
    }

    @Override
    public AuditIdentity convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AuditIdentity.parse(dbData);
    }
}
