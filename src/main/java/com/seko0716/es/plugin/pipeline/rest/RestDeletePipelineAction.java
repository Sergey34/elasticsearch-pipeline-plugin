package com.seko0716.es.plugin.pipeline.rest;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.INDEX;
import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.TYPE;
import static com.seko0716.es.plugin.pipeline.constants.RestConstants.PARAMS_PIPELINE_ID;
import static com.seko0716.es.plugin.pipeline.constants.RestConstants.PATH_WITH_ID;
import static org.elasticsearch.rest.RestRequest.Method.DELETE;

public class RestDeletePipelineAction extends BaseRestHandler {

    public RestDeletePipelineAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(DELETE, PATH_WITH_ID, this);
    }

    @Override
    public String getName() {
        return "delete pipeline";
    }

    @Override
    public RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) {
        DeleteRequest deletePipelineRequest = new DeleteRequest(INDEX, TYPE, request.param(PARAMS_PIPELINE_ID));
        return channel -> client.delete(deletePipelineRequest, new RestToXContentListener<>(channel));
    }
}