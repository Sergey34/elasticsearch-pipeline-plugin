package com.seko0716.es.plugin.pipeline.actions.output;

import com.seko0716.es.plugin.pipeline.actions.PipelineAction;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;

import java.util.Collections;
import java.util.Map;

public class ConsoleOutput extends PipelineAction {

    public ConsoleOutput(Map<String, Object> context, Map<String, Object> config) {
        super(context, config);
    }


    @Override
    protected Map<String, Object> action(Map<String, Object> event) {
        System.out.println(event);
        return Collections.emptyMap();
    }

    @Override
    protected void writeActionStatistic(Map<String, Object> event, Map<String, Object> actionResult, Throwable err) {
        Map<String, Object> stats = MapUtils.getMapOrEmpty(context, getActionName());
        Long count = (Long) stats.getOrDefault("count", 0L);
        stats.put("count", ++count);
        context.put(getActionName(),stats);
    }
}
