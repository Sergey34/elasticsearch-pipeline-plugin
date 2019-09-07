package com.seko0716.es.plugin.pipeline.services;

import org.elasticsearch.client.Client;

public enum ClientFactory {
    INSTANCE ();

    public Client getEsClient() {
        return esClient;
    }

    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    private  Client esClient;

}
