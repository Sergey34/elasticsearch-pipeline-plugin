package com.seko0716.es;

import com.seko0716.es.plugin.pipeline.module.ScheduleModule;
import com.seko0716.es.plugin.pipeline.rest.RestDeletePipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestExecutePipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestGetPipelineAction;
import com.seko0716.es.plugin.pipeline.rest.RestPostPipelineAction;
import com.seko0716.es.plugin.pipeline.services.ClientFactory;
import com.seko0716.es.plugin.pipeline.services.ScheduleService;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.watcher.ResourceWatcherService;

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
    public Collection<Object> createComponents(
            Client client,
            ClusterService clusterService,
            ThreadPool threadPool,
            ResourceWatcherService resourceWatcherService,
            ScriptService scriptService,
            NamedXContentRegistry xContentRegistry,
            Environment environment,
            NodeEnvironment nodeEnvironment,
            NamedWriteableRegistry namedWriteableRegistry) {
        Collection<Object> components = super.createComponents(client, clusterService, threadPool,
                resourceWatcherService, scriptService, xContentRegistry, environment, nodeEnvironment,
                namedWriteableRegistry);
        ClientFactory instance = ClientFactory.INSTANCE;
        instance.setEsClient(client);
        return components;
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
