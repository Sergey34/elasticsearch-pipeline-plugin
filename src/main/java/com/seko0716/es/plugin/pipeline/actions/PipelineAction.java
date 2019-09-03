package com.seko0716.es.plugin.pipeline.actions;

import com.seko0716.es.plugin.pipeline.utils.MapUtils;
import org.elasticsearch.common.Nullable;

import java.util.List;
import java.util.Map;

import static com.seko0716.es.plugin.pipeline.constants.FieldConstants.METADATA;
import static com.seko0716.es.plugin.pipeline.constants.FieldConstants.ORDER;
import static com.seko0716.es.plugin.pipeline.constants.FieldConstants.RELATED_FILTERS;

public abstract class PipelineAction implements Action, Comparable<PipelineAction> {
    protected final Map<String, Object> context;
    protected final Map<String, Object> config;

    public PipelineAction(Map<String, Object> context, Map<String, Object> config) {
        this.config = MapUtils.getMap(config, getActionName());
        this.context = context;
    }


    protected void perform(Map<String, Object> event) {
        if (isFiltered(event)) {
            return;
        }

        try {
            Map<String, Object> actionResult = action(event);
            writeActionStatistic(event, actionResult, null);
        } catch (Exception e) {
            writeActionStatistic(event, null, e);
        }
    }

    protected boolean isFiltered(Map<String, Object> event) {
        List<String> relatedFilters = MapUtils.getListOrDefault(config, RELATED_FILTERS, String.class);
        return MapUtils.getMapOrEmpty(event, METADATA).entrySet()
                .stream()
                .anyMatch(it -> relatedFilters.contains(it.getKey()) && MapUtils.getBoolean(it.getValue()));
    }

    protected abstract Map<String, Object> action(Map<String, Object> event);

     protected abstract void writeActionStatistic(Map<String, Object> event,
                                                 @Nullable Map<String, Object> actionResult,
                                                 @Nullable Throwable err);

    public Long getOrder() {
        return (Long) config.get(ORDER);
    }

    @Override
    public int compareTo(PipelineAction o) {
        return this.getOrder().compareTo(o.getOrder());
    }
}
