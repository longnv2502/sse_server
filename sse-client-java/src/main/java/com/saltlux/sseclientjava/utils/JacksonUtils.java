package com.saltlux.sseclientjava.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Slf4j
public final class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.USE_STD_BEAN_NAMING, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static String toJson(Object object) {
        try {
            if(null != object) {
                return objectMapper.writeValueAsString(object);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toMinifyJson(Object object) {
        try {
            if(null != object) {
                return objectMapper.writeValueAsString(object);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds object from given json
     *
     * @param json         json string
     * @param objectMapper ObjectMapper
     * @param valueType    object type to build
     * @param <T>          type of the object
     * @return Built object
     */
    public static <T> T getObject(String json, ObjectMapper objectMapper, Class<T> valueType) {
        if (StringUtils.isNullOrEmpty(json) || valueType == null) {
            return null;
        }
        try {
            return valueType.equals(String.class) ? (T) json : objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T getObject(String json, Class<T> valueType) {
        return getObject(json, objectMapper, valueType);
    }

    /**
     * Builds list of object from given json
     *
     * @param json         json string
     * @param objectMapper ObjectMapper
     * @param valueType    list type to build
     * @param <T>          type of the object
     * @return Built object
     */
    public static <T> List<T> getList(String json, ObjectMapper objectMapper, Class<T> valueType) {
        if (StringUtils.isNullOrEmpty(json) || valueType == null) {
            return null;
        }
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> getList(String json, Class<T> valueType) {
        return getList(json, objectMapper, valueType);
    }

    public static <T> String getJson(ObjectMapper objectMapper, T entity) {
        if(entity == null) {
            return null;
        }
        try {
            return entity instanceof String ? (String) entity : objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> String getJson(T entity) {
        return getJson(objectMapper, entity);
    }

    public static <T> Map<String, Object> getMap(T entity) {
        if(entity == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(entity, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Set given field value to given JSON
     *
     * @param fieldName  field name to be set
     * @param value      new value for field
     * @param jsonSource original JSON
     * @return new JSON string
     */
    public static String setNodeValue(ObjectMapper objectMapper, String fieldName, String value, String jsonSource)
            throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonSource);

        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                ((ObjectNode) node).put(fieldName, value);
            }
        } else {
            ((ObjectNode) jsonNode).put(fieldName, value);
        }

        return objectMapper.writeValueAsString(jsonNode);
    }

    public static String setNodeValue(String fieldName, String value, String jsonSource) throws IOException {
        return setNodeValue(objectMapper, fieldName, value, jsonSource);
    }

    /**
     * Set given field value to given JSON
     *
     * @param fieldName  field name to be set
     * @param jsonSource original JSON
     * @return new JSON string
     */
    public static String getNodeValue(ObjectMapper objectMapper, String fieldName, String jsonSource) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonSource);
        JsonNode fieldNode = null;
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                fieldNode = node.get(fieldName);
            }
        } else {
            fieldNode = jsonNode.get(fieldName);
        }

        if (fieldNode != null) {
            return fieldNode.asText();
        }
        return null;
    }

    public static String getNodeValue(String fieldName, String jsonSource) throws IOException {
        return getNodeValue(objectMapper, fieldName, jsonSource);
    }
}
