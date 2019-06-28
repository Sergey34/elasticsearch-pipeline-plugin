package com.seko0716.es.plugin.pipeline;

import com.seko0716.es.plugin.pipeline.module.ScheduleModule;
import com.seko0716.es.plugin.pipeline.rest.RestDeletePipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestExecutePipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestGetPipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestPostPipelineAction;
import com.seko0716.es.plugin.pipeline.services.ScheduleService;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;


public class PipelinePlugin extends Plugin implements ActionPlugin {


    @Override
    public Collection<Class<? extends LifecycleComponent>> getGuiceServiceClasses() {
        return Arrays.asList(ScheduleService.class);
    }

    @Override
    public Collection<Module> createGuiceModules() {
        return Arrays.asList(new ScheduleModule());
    }

    @Override
    public List<RestHandler> getRestHandlers(final Settings settings,
                                             final RestController restController,
                                             final ClusterSettings clusterSettings,
                                             final IndexScopedSettings indexScopedSettings,
                                             final SettingsFilter settingsFilter,
                                             final IndexNameExpressionResolver indexNameExpressionResolver,
                                             final Supplier<DiscoveryNodes> nodesInCluster) {


        return Arrays.asList(
                new RestGetPipelineAction(settings, restController),
                new RestDeletePipelineAction(settings, restController),
                new RestExecutePipelineAction(settings, restController),
                new RestPostPipelineAction(settings, restController)
        );
    }


}
