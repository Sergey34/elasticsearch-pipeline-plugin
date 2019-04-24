package com.seko0716.es.actions;

import org.elasticsearch.client.node.NodeClient;

import java.util.Map;

public interface PipelineAction {
    String getType();

    String getGroup();

    void perform(Map<String, Object> context, NodeClient client);
}
