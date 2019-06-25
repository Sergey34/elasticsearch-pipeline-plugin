package com.seko0716.es.plugin.pipeline.actions;

public interface Action {
    default String getActionName() {
        return getClass().getSimpleName();
    }
}
