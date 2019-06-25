package com.seko0716.es.plugin.pipeline;

import com.seko0716.es.plugin.pipeline.actions.Pipeline;
import com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr;
import com.seko0716.es.plugin.pipeline.actions.finish.ConsoleFinish;
import com.seko0716.es.plugin.pipeline.actions.finish.Finish;
import com.seko0716.es.plugin.pipeline.actions.input.DemoInput;
import com.seko0716.es.plugin.pipeline.actions.input.Input;
import com.seko0716.es.plugin.pipeline.actions.output.ConsoleOutput;
import com.seko0716.es.plugin.pipeline.actions.output.ErrorConsoleOutput;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ActionService {
    public static final HashMap<String, Object> CONTEXT = new HashMap<>();
    public static final HashMap<String, Object> CONFIG = new HashMap<>();

    static {
        Map<String, Object> value = new HashMap<>();
        value.put("count", 10L);
        CONFIG.put(DemoInput.class.getSimpleName(), value);
        CONFIG.put(ConsoleOutput.class.getSimpleName(), MapUtils.getMap("relatedFilters", Arrays.asList("RandomFilterErr_id")));
        CONFIG.put(ErrorConsoleOutput.class.getSimpleName(), new HashMap<>());
        CONFIG.put(RandomFilterErr.class.getSimpleName(), MapUtils.getMap("id", "RandomFilterErr_id"));

    }

    private Consumer<Map<String, Object>> pipeline = new Pipeline(CONTEXT, CONFIG);
    private Input input = new DemoInput(CONTEXT, CONFIG);
    private List<Finish> finishActions = Arrays.asList(new ConsoleFinish(CONTEXT, CONFIG));


    public static void main(String[] args) {
        ActionService actionService = new ActionService();
        Input input = actionService.input;
        Consumer<Map<String, Object>> pipeline = actionService.pipeline;


        List<Map<String, Object>> dataset;
        while ((dataset = input.get()) != null) {
            dataset.forEach(pipeline);
        }
        List<Finish> finishActions = actionService.finishActions;
        finishActions.forEach(Finish::perform);

    }
}
