package com.seko0716.es.plugin.pipeline.actions.output;

import com.seko0716.es.plugin.pipeline.actions.PipelineAction;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class ErrorConsoleOutput extends PipelineAction {
    private final Logger logger = LogManager.getLogger(getClass());

    @Override
    protected Map<String, Object> action(Map<String, Object> event) {
        logger.error("err {}", event);
        return Collections.emptyMap();
    }

    @Override
    protected void writeActionStatistic(Map<String, Object> event, Map<String, Object> actionResult, Throwable err) {
        Map<String, Object> stats = MapUtils.getMapOrEmpty(context, getActionName());
        Long count = (Long) stats.getOrDefault("count", 0L);
        stats.put("count", ++count);
        context.put(getActionName(), stats);
    }

    @Override
    public String getGroup() {
        return "output";
    }
}
