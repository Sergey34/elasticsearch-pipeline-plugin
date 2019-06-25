package com.seko0716.es.plugin.pipeline.actions.output;

import java.util.HashMap;
import java.util.Map;

public class ConsoleOutput implements Output {
    @SuppressWarnings("unchecked")
    @Override
    public void perform(Map<String, Object> context, Map<String, Object> event) {
        System.out.println(event);
        Map<String, Object> stats = (Map<String, Object>) context.getOrDefault(getActionName(), new HashMap<String, Object>());
        Long count = (Long) stats.getOrDefault("count", 0L);
        stats.put("count", ++count);
    }
}
