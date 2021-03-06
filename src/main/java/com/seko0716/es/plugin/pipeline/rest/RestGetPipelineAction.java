package com.seko0716.es.plugin.pipeline.rest;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import static com.seko0716.es.plugin.pipeline.constants.IndexConstants.INDEX;
import static com.seko0716.es.plugin.pipeline.constants.RestConstants.PATH;
import static org.elasticsearch.rest.RestRequest.Method.GET;


public class RestGetPipelineAction extends BaseRestHandler {

    public RestGetPipelineAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, PATH, this);
    }

    @Override
    public String getName() {
        return "get pipelines";
    }

    @Override
    public RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) {
        SearchRequest pipelineSearchAllRequest = new SearchRequest(INDEX);
        return channel -> client.search(pipelineSearchAllRequest, new RestToXContentListener<>(channel));
    }
}