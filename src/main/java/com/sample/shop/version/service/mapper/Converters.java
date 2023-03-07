package com.sample.shop.version.service.mapper;

import com.sample.shop.version.service.dto.RevisionTypeDto;
import org.hibernate.envers.RevisionType;

public class Converters {

    public static RevisionTypeDto convert(RevisionType source) {
        if (source == null) {
            return null;
        }

        switch (source) {
            case ADD:
                return RevisionTypeDto.ADD;
            case MOD:
                return RevisionTypeDto.MOD;
            case DEL:
                return RevisionTypeDto.DEL;
            default:
                throw new IllegalArgumentException("source");
        }
    }
}
