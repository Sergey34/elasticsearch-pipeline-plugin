package com.seko0716.es.plugin.pipeline.module;

import com.seko0716.es.plugin.pipeline.services.ScheduleService;
import org.elasticsearch.common.inject.AbstractModule;

public class ScheduleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduleService.class).asEagerSingleton();
    }
}