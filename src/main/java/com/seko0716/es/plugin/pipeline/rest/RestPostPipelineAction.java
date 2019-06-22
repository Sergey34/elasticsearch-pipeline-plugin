package com.seko0716.es.plugin.pipeline.rest;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.io.IOException;
import java.util.Map;

import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.INDEX;
import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.TYPE;
import static org.elasticsearch.rest.RestRequest.Method.POST;

public class RestPostPipelineAction extends BaseRestHandler {

    public RestPostPipelineAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(POST, "/_pipelines/{pipeline_id}", this);
    }

    @Override
    public String getName() {
        return "Create or update pipeline";
    }

    @Override
    public RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) throws IOException {
        String pipelineId = request.param("pipeline_id");

        try (XContentParser xContentParser = request.contentParser()) {
            Map<String, Object> pipelineBody = xContentParser.mapOrdered();
            UpdateRequest createOrUpdateRequest = new UpdateRequest(INDEX, TYPE, pipelineId);
            createOrUpdateRequest.docAsUpsert(true);
            createOrUpdateRequest.doc(pipelineBody);
            return channel -> client.update(createOrUpdateRequest, new RestToXContentListener<>(channel));
        }
    }
}