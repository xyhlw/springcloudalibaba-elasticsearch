package com.xy.cloud.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchAPIConfig {


    HttpHost[] hosts = {new HttpHost("10.132.192.138", 9200, "http"),
            new HttpHost("192.168.192.137", 9200, "http"),
            new HttpHost("192.168.192.134", 9200, "http")};

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hosts));
        return client;
    }
}


