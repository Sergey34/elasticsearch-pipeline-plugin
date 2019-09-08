package com.seko0716.es.plugin.pipeline.domains;

import com.seko0716.es.plugin.pipeline.actions.Pipeline;
import com.seko0716.es.plugin.pipeline.actions.finish.FinishAction;
import com.seko0716.es.plugin.pipeline.actions.input.Input;
import com.seko0716.es.plugin.pipeline.services.ActionService;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PipelineJob extends AbstractJob {
    @Override
    protected void action(Map<String, Object> config) {
        Map<String, Object> inputConfig = MapUtils.getMap(config, "input_action");

        final Map<String, Object> pipelineActionsConfig = new HashMap<>();
        List<Map<String, Object>> filterActions = MapUtils.getListOfMap(config, "filter_actions");
        List<Map<String, Object>> outputActions = MapUtils.getListOfMap(config, "output_actions");
        List<Map<String, Object>> joinActions = MapUtils.getListOfMap(config, "join_actions");
        List<Map<String, Object>> finalActions = MapUtils.getListOfMap(config, "final_actions");

        pipelineActionsConfig.put("filters", filterActions);
        pipelineActionsConfig.put("outputs", outputActions);
        pipelineActionsConfig.put("joins", joinActions);

        final Map<String, Object> context = new HashMap<>();


        Consumer<Map<String, Object>> pipeline = new Pipeline(context, pipelineActionsConfig);

        Input input = ActionService.loadAction(MapUtils.getString(inputConfig, "class"), Input.class);
        input.setConfiguration(inputConfig);
        input.setContext(context);
        input.setClient(client);

        List<FinishAction> finish = finalActions.stream()
                .map(it -> it.get("class").toString())
                .map(it -> ActionService.loadAction(it, FinishAction.class))
                .sorted()
                .peek(it -> {
                    it.setConfiguration(inputConfig);
                    it.setContext(context);
                    it.setClient(client);
                })
                .collect(Collectors.toList());
        ActionService actionService = new ActionService();
        actionService.executePipeline(input, pipeline, finish);
    }
}
