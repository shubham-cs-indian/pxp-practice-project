package com.cs.core.elastic.services;

import com.cs.core.rdbms.driver.RDBMSLogger;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ScrollUtils {

  public static <T> void scrollThrough(Function<SearchHit, T> functionality, RestHighLevelClient client, List<String> indices, QueryBuilder query, List<String> docTypes) throws IOException
  {
    try {
      final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(60L));
      SearchRequest searchRequest = new SearchRequest(indices.toArray(String[]::new));
      searchRequest.scroll(scroll);
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(query).size(100);
      searchRequest.source(searchSourceBuilder);
      searchRequest.types(docTypes.toArray(new String[0]));

      //max of 1000 hits will be returned for each scroll
      //Scroll until no hits are returneds

      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      String scrollId = searchResponse.getScrollId();
      SearchHit[] searchHits = searchResponse.getHits().getHits();

      while (searchHits != null && searchHits.length > 0) {
        for (SearchHit hit : searchHits) {
          functionality.apply(hit);
        }

        //new Scroll Request
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(scroll);
        searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
        scrollId = searchResponse.getScrollId();
        searchHits = searchResponse.getHits().getHits();
      }
    }
    catch(Exception e){
      RDBMSLogger.instance().exception(e);
    }
  }
  
  public static <T> void scrollThrough(Function<SearchHit[], T> functionality, RestHighLevelClient client,
      List<String> indices, QueryBuilder query, List<String> docTypes, Long skipBatchNumber, int size) throws IOException
  {
    final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
    SearchRequest searchRequest = new SearchRequest(indices.toArray(String[]::new));
    searchRequest.scroll(scroll);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(query);
    searchSourceBuilder.size(size);
    searchRequest.source(searchSourceBuilder);
    searchRequest.types(docTypes.toArray(new String[0]));

    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    String scrollId = searchResponse.getScrollId();
    SearchHit[] searchHits = searchResponse.getHits().getHits();

    while (searchHits != null && searchHits.length > 0) {
      if(skipBatchNumber == 0) {
        functionality.apply(searchHits);
      }

      SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
      scrollRequest.scroll(scroll);
      searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
      scrollId = searchResponse.getScrollId();
      searchHits = searchResponse.getHits().getHits();
      
      if(skipBatchNumber != 0) {
        skipBatchNumber--;
      }
    }
  }
}
