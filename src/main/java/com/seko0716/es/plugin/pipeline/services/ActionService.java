package com.seko0716.es.plugin.pipeline.services;


import com.seko0716.es.plugin.pipeline.actions.Action;
import com.seko0716.es.plugin.pipeline.actions.finish.Finish;
import com.seko0716.es.plugin.pipeline.actions.finish.FinishAction;
import com.seko0716.es.plugin.pipeline.actions.input.Input;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class ActionService {
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

    public static List<Action> loadActions() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections("com.seko0716.es.plugin.pipeline.actions");
        Set<Class<? extends Action>> classes = reflections.getSubTypesOf(Action.class);
        List<Action> actions = new ArrayList<>();
        for (Class<? extends Action> it : classes) {
            Optional<Constructor<?>> constructor = Arrays.stream(it.getConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 0)
                    .findAny();
            Constructor<?> noAuthenticator = constructor.orElseThrow(() -> new IllegalStateException("No authenticator set"));
            Action action = (Action) noAuthenticator.newInstance();
            actions.add(action);
        }
        return actions;
    }

    public static List<Input> loadInputActions() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections("com.seko0716.es.plugin.pipeline.actions");
        Set<Class<? extends Input>> classes = reflections.getSubTypesOf(Input.class);
        List<Input> actions = new ArrayList<>();
        for (Class<? extends Input> it : classes) {
            Optional<Constructor<?>> constructor = Arrays.stream(it.getConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 0)
                    .findAny();
            Constructor<?> noAuthenticator = constructor.orElseThrow(() -> new IllegalStateException("No authenticator set"));
            Input action = (Input) noAuthenticator.newInstance();
            actions.add(action);
        }
        return actions;
    }

    public static List<Finish> loadFinishActions() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections("com.seko0716.es.plugin.pipeline.actions");
        Set<Class<? extends Finish>> classes = reflections.getSubTypesOf(Finish.class);
        List<Finish> actions = new ArrayList<>();
        for (Class<? extends Finish> it : classes) {
            Optional<Constructor<?>> constructor = Arrays.stream(it.getConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 0)
                    .findAny();
            Constructor<?> noAuthenticator = constructor.orElseThrow(() -> new IllegalStateException("No authenticator set"));
            Finish action = (Finish) noAuthenticator.newInstance();
            actions.add(action);
        }
        return actions;
    }
}