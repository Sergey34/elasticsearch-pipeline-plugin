package com.seko0716.es.plugin.pipeline.services;

import com.seko0716.es.plugin.pipeline.actions.Pipeline;
import com.seko0716.es.plugin.pipeline.actions.filter.RandomFilterErr;
import com.seko0716.es.plugin.pipeline.actions.finish.ConsoleFinish;
import com.seko0716.es.plugin.pipeline.actions.finish.FinishAction;
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

//import org.junit.Test;

public class ActionServiceTest {
    private final ActionService actionService = new ActionService();

    //    @Test
    public void testFooBar() {
        final HashMap<String, Object> CONTEXT = new HashMap<>();
        final HashMap<String, Object> CONFIG = new HashMap<>();

        Map<String, Object> value = new HashMap<>();
        value.put("count", 10L);
        CONFIG.put(DemoInput.class.getSimpleName(), value);
        CONFIG.put(ConsoleOutput.class.getSimpleName(), MapUtils.getMap("relatedFilters", Arrays.asList("RandomFilterErr_id")));
        CONFIG.put(ErrorConsoleOutput.class.getSimpleName(), new HashMap<>());
        CONFIG.put(RandomFilterErr.class.getSimpleName(), MapUtils.getMap("id", "RandomFilterErr_id"));


        Consumer<Map<String, Object>> pipeline = new Pipeline(CONTEXT, CONFIG);
        Input input = new DemoInput();
        List<FinishAction> finishActions = Arrays.asList(new ConsoleFinish(CONTEXT, CONFIG));


        actionService.executePipeline(input, pipeline, finishActions);
    }

    public static void main(String[] args) {
        new ActionServiceTest().testFooBar();
    }
}
