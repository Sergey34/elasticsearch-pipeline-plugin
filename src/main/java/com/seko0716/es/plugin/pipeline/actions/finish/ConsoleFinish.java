package com.seko0716.es.plugin.pipeline.actions.finish;

import java.util.Map;

public class ConsoleFinish implements Finish {
    private final Map<String, Object> context;
    private final Map<String, Object> config;

    public ConsoleFinish(Map<String, Object> context, Map<String, Object> config) {
        this.context = context;
        this.config = config;
    }

    @Override
    public void perform() {
        System.out.println(context);
    }
}
