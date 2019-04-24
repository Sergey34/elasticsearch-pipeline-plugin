package com.seko0716.es.actions.input;

import com.seko0716.es.actions.PipelineAction;
import org.elasticsearch.client.node.NodeClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchIndexesPipelineAction implements PipelineAction {
    private static final String TYPE = "search_indexes";
    private static final String GROUP = "input";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getGroup() {
        return GROUP;
    }

    private void tryPerform(Map<String, Object> context, NodeClient client) {
        List<Map<String, Object>> actions = (List<Map<String, Object>>) context.get("actions");
        actions.stream()
                .filter(action -> getType().equals(action.get("group")) && getGroup().equals(action.get("type")))
                .findFirst()
                .map(action -> (Map<String, Object>) action.get("config"))
                .ifPresent(config -> {
                    List<String> searchTemplates = (List<String>) config.get("searchTemplates");
                    String[] indices = client.admin().indices().prepareGetIndex()
                            .setIndices(searchTemplates.toArray(new String[0]))
                            .get().getIndices();

                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> searchIndexesData = new HashMap<>();
                    searchIndexesData.put("status", "success");
                    searchIndexesData.put("searchedIndexes", indices);
                    data.put("searchedIndexes", searchIndexesData);

                    context.put("data", data);
                });
    }

    @Override
    public void perform(Map<String, Object> context, NodeClient client) {
        try {
            tryPerform(context, client);
        } catch (Exception e) {
            Map<String, Object> searchIndexesData = new HashMap<>();
            searchIndexesData.put("status", "success");
            searchIndexesData.put("exception", e);

            context.put("data", searchIndexesData);
            context.put("pipelineFailed", true);
        }
    }
}
