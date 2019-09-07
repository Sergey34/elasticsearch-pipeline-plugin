package com.seko0716.es.plugin.pipeline.actions;

import com.seko0716.es.plugin.pipeline.services.ActionService;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;
import org.elasticsearch.client.Client;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Pipeline implements Consumer<Map<String, Object>> {
    private static final List<Action> loadActions;
    private List<PipelineAction> actions;
    private Client client;

    static {
        try {
            loadActions = ActionService.loadActions();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("can not load actions", e);
        }
    }

    public Pipeline(final Map<String, Object> context, Map<String, Object> config) {
        List<String> classes = config.values().stream()
                .flatMap(it ->  MapUtils.generifyListOfMap(it).stream())
                .map(it -> MapUtils.getString(it, "class"))
                .collect(Collectors.toList());

        this.actions = loadActions.stream()
                .filter(it -> classes.contains(it.getActionName()))
                .map(it -> (PipelineAction) it)
                .sorted()
                .peek(it -> {
                    String group = it.getGroup();
                    List<Map<String, Object>> actionsConfig = MapUtils.getListOfMap(config, group);
                    Map<String, Object> configuration = MapUtils.generifyToMap(actionsConfig.stream()
                            .filter(c -> it.getActionName().equals(MapUtils.getString(MapUtils.generifyToMap(c), "class")))
                            .findAny()
                            .orElseThrow(() -> new IllegalStateException("can not find configuration")));
                    it.setConfiguration(MapUtils.generifyToMap(configuration));
                    it.setContext(context);
                    it.setClient(client);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void accept(Map<String, Object> event) {
        actions.forEach(it -> it.perform(event));
    }
}
