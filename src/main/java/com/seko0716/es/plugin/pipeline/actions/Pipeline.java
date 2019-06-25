package com.seko0716.es.plugin.pipeline.actions;

import com.seko0716.es.plugin.pipeline.actions.output.ConsoleOutput;
import com.seko0716.es.plugin.pipeline.actions.output.ErrorConsoleOutput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Pipeline implements Consumer<Map<String, Object>> {
    private List<PipelineAction> actions = Arrays.asList(new ConsoleOutput(),new ErrorConsoleOutput());
    private final Map<String, Object> context;

    public Pipeline(Map<String, Object> context, HashMap<String, Object> config) {
        this.context = context;
    }

    @Override
    public void accept(Map<String, Object> event) {
        actions.forEach(it-> it.perform(context, event));
    }
}
