package com.seko0716.es.plugin.pipeline.actions.finish;

import java.util.Map;

public interface Finish {
    Map<String, Object> action();

    default String getActionName() {
        return getClass().getSimpleName();
    }
}
