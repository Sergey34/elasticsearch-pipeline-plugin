package com.seko0716.es.plugin.pipeline.actions.filter;

import com.seko0716.es.plugin.pipeline.actions.PipelineAction;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;
import org.elasticsearch.common.Randomness;

import java.util.Map;
import java.util.Random;

public class RandomFilterErr extends PipelineAction {

    public static final Random RANDOM = Randomness.get();

    @Override
    protected Map<String, Object> action(Map<String, Object> event) {
        boolean isFiltered = RANDOM.nextBoolean();
        if (isFiltered) {
            event.put("@metadata", MapUtils.getMap(config.get("id").toString(), true));
        }
        return MapUtils.getMap("isFiltered", isFiltered);
    }

    @Override
    protected void writeActionStatistic(Map<String, Object> event, Map<String, Object> actionResult, Throwable err) {

    }

    @Override
    public String getGroup() {
        return "filter";
    }
}
