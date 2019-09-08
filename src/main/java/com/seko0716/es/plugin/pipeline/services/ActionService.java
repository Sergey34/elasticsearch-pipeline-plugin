package com.seko0716.es.plugin.pipeline.services;


import com.seko0716.es.plugin.pipeline.actions.finish.FinishAction;
import com.seko0716.es.plugin.pipeline.actions.input.Input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionService {

    private static final Function<Constructor<?>, ?> CONSTRUCTOR_FUNCTION = it -> {
        try {
            return it.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("can not create instance", e);
        }
    };

    public ActionService() {
        // TODO: 28.06.19 add listener (dead nodes or add pipeline) and reload pipelines
    }

    public void executePipeline(Input input, Consumer<Map<String, Object>> pipeline, List<FinishAction> finishActions) {
        List<Map<String, Object>> dataset;
        while ((dataset = input.get()) != null) {
            dataset.forEach(pipeline);
        }
        finishActions.forEach(FinishAction::perform);
    }

    public static <T> T loadAction(String aClass, Class<T> clazz) {
        try {
            Class<?> actionClass = Class.forName(aClass);
            return Arrays.stream(actionClass.getConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 0)
                    .findAny()
                    .map(CONSTRUCTOR_FUNCTION)
                    .map(clazz::cast)
                    .orElseThrow(() -> new IllegalStateException("can not fount action"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("can not fount action", e);
        }
    }
}