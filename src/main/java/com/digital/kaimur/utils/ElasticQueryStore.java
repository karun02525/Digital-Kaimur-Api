package com.digital.kaimur.utils;

import com.digital.kaimur.models.elastic.StoreElasticModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ElasticQueryStore {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;


    public void createStoreElastic(StoreElasticModel store) {
        try {
            Map dataMap = objectMapper.convertValue(store, Map.class);
            IndexRequest indexRequest = new IndexRequest(ElasticKey.storeIndex);
            indexRequest.id(store.getSid());
            indexRequest.source(dataMap);
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (IOException ex) {
            ex.getLocalizedMessage();
        }
    }

    public List<StoreElasticModel> getAllStore() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ElasticKey.storeIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        List<StoreElasticModel> lresult = null;
        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        lresult = getSearchResult(searchResponse);
        return lresult;
    }


    public List<StoreElasticModel> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<StoreElasticModel> _doc = new ArrayList<>();
        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> _doc.add(objectMapper.convertValue(hit.getSourceAsMap()
                            , StoreElasticModel.class)));
        }
        return _doc;
    }


    //Update Profile Image
    public void updateProfileImageElastic(String id, String field, String value) {
        try {
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(ElasticKey.storeIndex);
            updateRequest.id(id);
            updateRequest.doc(jsonBuilder
                    .startObject()
                    .field(field, value)
                    .endObject());
            client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }

    }



}