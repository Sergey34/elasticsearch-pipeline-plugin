package com.seko0716.es.rest;

import com.seko0716.es.actions.PipelineAction;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public class RestPipelineExecutionAction extends BaseRestHandler {
    private static final String CONFIG_INDEX = ".pipeline";
    private static final String TYPE = "doc";
    private Collection<PipelineAction> pipelineActions;

    public RestPipelineExecutionAction(final Settings settings,
                                       final RestController controller) {
        super(settings);
        controller.registerHandler(GET, "/_pipeline/{pipeline_id}", this);
        //todo initialize pipelineActions
    }

    @Override
    public String getName() {
        return "pipeline";
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request,
                                                 final NodeClient client) throws IOException {


        // todo get pipeline by id
        final String pipelineId = request.param("pipeline_id");

        Map<String, Object> pipeline = client.prepareGet(CONFIG_INDEX, TYPE, pipelineId)
                .get()
                .getSourceAsMap();

        pipelineActions.forEach(pipelineAction -> pipelineAction.perform(pipeline, client));

        //todo create pipeline context
        //todo perform context on actions

        final boolean isPretty = request.hasParam("pretty");

        return channel -> {
            final XContentBuilder builder = JsonXContent.contentBuilder();
            if (isPretty) {
                builder.prettyPrint().lfAtEnd();
            }
            builder.startObject();
            if (pipelineId != null) {
                builder.field("index", pipelineId);
            }
            builder.field("description",
                    "This is a sample response: " + new Date().toString());
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(OK, builder));
        };
    }
}
