package com.seko0716.es.plugin.pipeline.actions;

import com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr;
import com.seko0716.es.plugin.pipeline.actions.output.ConsoleOutput;
import com.seko0716.es.plugin.pipeline.actions.output.ErrorConsoleOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Pipeline implements Consumer<Map<String, Object>> {
    private List<PipelineAction> actions;

    public Pipeline(Map<String, Object> context, Map<String, Object> config) {
        // TODO: 25.06.19 create new action by configuration
        actions = Arrays.asList(
                new RandomFilterErr(context, config),
                new ConsoleOutput(context, config),
                new ErrorConsoleOutput(context, config));
    }

    @Override
    public void accept(Map<String, Object> event) {
        actions.forEach(it -> it.perform(event));
    }
}
