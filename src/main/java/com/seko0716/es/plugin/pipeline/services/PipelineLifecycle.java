package com.seko0716.es.plugin.pipeline.services;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.slice.SliceBuilder;
import org.quartz.JobDetail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.INDEX;
import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.SIZE;

public class PipelineLifecycle extends AbstractLifecycleComponent implements ClusterStateListener {
    private final AtomicInteger sliceSize = new AtomicInteger(1);
    private final AtomicInteger sliceId = new AtomicInteger(0);
    private final ScheduleService scheduleService;
    private final Client client;

    @Override
    public void clusterChanged(ClusterChangedEvent event) {
        if (event.nodesChanged()) {
            ImmutableOpenMap<String, DiscoveryNode> ingestNodesPreviousState = event.previousState().nodes().getIngestNodes();
            ImmutableOpenMap<String, DiscoveryNode> ingestNodesCurrentState = event.state().nodes().getIngestNodes();
            DiscoveryNode localNode = event.state().nodes().getLocalNode();

            if (localNode.isIngestNode() && !ingestNodesCurrentState.equals(ingestNodesPreviousState)) {
                this.sliceSize.set(ingestNodesCurrentState.size());
                List<String> nodeIds = Arrays.asList(ingestNodesCurrentState.keys().toArray(String.class));
                nodeIds.sort(String::compareTo);
                this.sliceId.set(nodeIds.indexOf(localNode.getId()));

                doStart();
            }
        }
    }

    @Inject
    protected PipelineLifecycle(Settings settings, Client client, ScheduleService scheduleService) {
        super(settings);
        this.client = client;
        this.scheduleService = scheduleService;

    }

    @Override
    protected void doStart() {
        scheduleService.clear();
        SearchResponse searchResponse = client
                .prepareSearch(INDEX)
                .setSize(SIZE)
                .setScroll(new TimeValue(30000))
                .slice(new SliceBuilder(sliceId.get(), sliceSize.get()))
                .get();

        do {
            Arrays.stream(searchResponse
                    .getHits()
                    .getHits())
                    .map(SearchHit::getSourceAsMap)
                    .map(this::toJob)
                    .forEach(scheduleService::addOrReplaceJob);


            searchResponse = client
                    .prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
        } while (searchResponse.getHits().getHits().length == 0);
    }

    private JobDetail toJob(Map<String, Object> it) {
        // TODO: 29.06.19 map to Input Pipeline Finish action
        return null;
    }

    @Override
    protected void doStop() {
        scheduleService.clear();
    }

    @Override
    protected void doClose() {
        scheduleService.clear();
    }
}
