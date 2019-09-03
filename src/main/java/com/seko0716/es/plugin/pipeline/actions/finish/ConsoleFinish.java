package com.seko0716.es.plugin.pipeline.actions.finish;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ConsoleFinish implements Finish {
    private final Map<String, Object> context;
    private final Map<String, Object> config;
    private final Logger logger;

    public ConsoleFinish(Map<String, Object> context, Map<String, Object> config) {
        this.context = context;
        this.config = config;
        this.logger = LogManager.getLogger(getClass());
    }

    @Override
    public void perform() {
        logger.info("context {}", context);
    }
}
