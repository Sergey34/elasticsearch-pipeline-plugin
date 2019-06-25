package com.seko0716.es.plugin.pipeline.actions.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoInput implements Input {
    private final Map<String, Object> context;
    private final Map<String, Object> config;

    @SuppressWarnings("unchecked")
    public DemoInput(Map<String, Object> context, Map<String, Object> config) {
        this.context = context;
        this.config = (Map<String, Object>) config.get(getActionName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> get() {
        Long count = (Long) config.get("count");
        Map<String, Object> stats = (Map<String, Object>) context.getOrDefault(getActionName(), new HashMap<String, Object>());
        Long contextCount = (Long) stats.getOrDefault("count", 0L);
        if (contextCount >= count) {
            return null;
        }


        Map<String, Object> map1 = new HashMap<>();
        map1.put("1", 1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("1", 2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("1", 3);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("1", 4);
        Map<String, Object> map5 = new HashMap<>();
        map5.put("1", 5);
        Map<String, Object> map6 = new HashMap<>();
        map6.put("1", 6);

        List<Map<String, Object>> result = Arrays.asList(map1, map2, map3, map4, map5, map6);
        stats.put("count", contextCount + result.size());
        context.put(getActionName(),stats);
        return result;
    }
}
