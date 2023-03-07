package com.sample.shop.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class GenericUtils {

    public static <T> List<T> loadObjectList(Class<T> type, File fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
            CsvMapper mapper = new CsvMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

            MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(fileName);
            return readValues.readAll();
        } catch (Exception e) {
            System.err.println("Error occurred while loading object list from file " + fileName);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
