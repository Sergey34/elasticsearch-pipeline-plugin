package com.seko0716.es.plugin.pipeline.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MapUtils {
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> sourceMap, String key) {
        return (Map<String, Object>) sourceMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapOrEmpty(Map<String, Object> sourceMap, String key) {
        return (Map<String, Object>) sourceMap.getOrDefault(key, new HashMap<>());
    }

    public static Map<String, Object> getMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <T> List<T> getListOrDefault(Map<String, Object> sourceMap, String key, Class<T> clazz) {
        return ((List<?>) sourceMap.getOrDefault(key, new ArrayList<>()))
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    public static boolean getBoolean(Object value) {
        if (value instanceof Boolean)
            return (boolean) value;
        return false;
    }
}
