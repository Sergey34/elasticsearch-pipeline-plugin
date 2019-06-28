package com.seko0716.es.plugin.pipeline.services;

import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.function.Supplier;

public class PipelineLifecycle extends AbstractLifecycleComponent implements ClusterStateListener {

    @Override
    public void clusterChanged(ClusterChangedEvent event) {
        if (event.nodesChanged()) {
            ClusterState state = event.state();
            ImmutableOpenMap<String, DiscoveryNode> ingestNodes = state.nodes().getIngestNodes();
            // TODO: 28.06.19 load pipelines for this node
            // TODO: 28.06.19 registry pipelines
        }
    }

    @Inject
    protected PipelineLifecycle(Settings settings, Client client, Supplier<DiscoveryNodes> nodesInCluster) {
        super(settings);
    }

    @Override
    protected void doStart() {
        // TODO: 28.06.19 load pipelines for this node
        // TODO: 28.06.19 registry pipelines
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() throws IOException {

    }
}
