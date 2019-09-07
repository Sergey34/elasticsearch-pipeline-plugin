package com.seko0716.es.plugin.pipeline.actions;

import org.elasticsearch.client.Client;

import java.util.Map;

public interface Action {
    default String getActionName() {
        return getClass().getSimpleName();
    }

    void setConfiguration(Map<String, Object> configuration);

    void setContext(Map<String, Object> context);

    void setClient(Client client);
}
