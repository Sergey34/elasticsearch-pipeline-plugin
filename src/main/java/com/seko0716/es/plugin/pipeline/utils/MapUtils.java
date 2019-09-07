package com.seko0716.es.plugin.pipeline.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SuppressWarnings({"SimplifyStreamApiCallChains", "unchecked"})
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

    public static boolean getBoolean(Map<String, Object> sourceMap, String key, Boolean value) {
        return (boolean) sourceMap.getOrDefault(key, value);
    }

    public static boolean getBoolean(Object value) {
        if (value instanceof Boolean)
            return (boolean) value;
        return false;
    }

    public static String getString(Map<String, Object> config, String key) {
        return (String) config.get(key);
    }

    public static Map<String, Object> generifyToMap(Object map) {
        return (Map<String, Object>) map;
    }

    public static List<Map<String, Object>> getListOfMap(Map<String, Object> sourceMap, String key) {
        return ((List<Map<String, Object>>) sourceMap.getOrDefault(key, new ArrayList<>()))
                .stream()
                .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> generifyListOfMap(Object it) {
        return (List<Map<String, Object>>) it;
    }
}
