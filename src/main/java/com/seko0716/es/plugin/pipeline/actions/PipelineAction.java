package com.seko0716.es.plugin.pipeline.actions;

import java.util.Map;

public interface PipelineAction extends Action {
    void perform(Map<String, Object> context, Map<String, Object> event);
}
