package com.seko0716.es.plugin.pipeline.actions.input;

import org.elasticsearch.client.Client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoInput implements Input {
    private Map<String, Object> context;
    private Map<String, Object> config;
    private Client client;

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
        context.put(getActionName(), stats);
        return result;
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        this.config = configuration;
    }

    @Override
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    @Override
    public void setClient(Client client) {
        this.client=client;
    }
}
