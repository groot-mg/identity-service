package com.generoso.identity.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String asString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Error converting object to json string: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException ex) {
            log.error("Error converting object to json string: {}", ex.getMessage());
            throw new RuntimeException("Error reading json value", ex);
        }
    }
}
