package com.seko0716.es.plugin.pipeline.services;


import com.seko0716.es.plugin.pipeline.actions.finish.Finish;
import com.seko0716.es.plugin.pipeline.actions.input.Input;
import com.seko0716.es.plugin.pipeline.constants.IndexConstants;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ActionService {
    private final Client client;
    private final ScheduleService scheduleService;

    @Inject
    public ActionService(Client client, ScheduleService scheduleService) {
        this.client = client;
        this.scheduleService = scheduleService;
        // TODO: 28.06.19 add listener (dead nodes or add pipeline) and reload pipelines
    }

    public void tryExecutePipeline(String pipelineId) {
        Map<String, Object> pipelineSource = client.prepareGet(IndexConstants.INDEX, IndexConstants.TYPE, pipelineId)
                .get()
                .getSource();
        boolean isActivePipeline = validatePipeline(pipelineSource);
        if (isActivePipeline) {
            return;
        }

        Input input = createInputAction(pipelineSource);
        Consumer<Map<String, Object>> pipeline = createPipeline(pipelineSource);
        List<Finish> finish = createFinishAction(pipelineSource);

        executePipeline(input, pipeline, finish);

    }

    protected void executePipeline(Input input, Consumer<Map<String, Object>> pipeline, List<Finish> finishActions) {
        List<Map<String, Object>> dataset;
        while ((dataset = input.get()) != null) {
            dataset.forEach(pipeline);
        }
        finishActions.forEach(Finish::perform);
    }

    private List<Finish> createFinishAction(Map<String, Object> pipelineSource) {
        return null;
    }

    private Consumer<Map<String, Object>> createPipeline(Map<String, Object> pipelineSource) {
        return null;
    }

    private Input createInputAction(Map<String, Object> pipelineSource) {
        return null;
    }

    private boolean validatePipeline(Map<String, Object> pipelineSource) {
        return false;
    }

}