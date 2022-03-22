package com.cs.core.elastic.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import com.cs.core.elastic.aggregation.BaseAggregation.PropertyAggName;
import com.cs.core.elastic.aggregation.BaseAggregation.RangeAggName;
import com.cs.core.elastic.builders.Aggregator;
import com.cs.core.elastic.builders.QueryBuilder;
import com.cs.core.elastic.builders.SearchBuilder;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.ibuilders.IAggregator;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.elastic.iservices.ISearchExecutor;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO.AggregationType;

public class SearchExecutor implements ISearchExecutor {

  public String[] indices;

  public SearchExecutor(String... indices)
  {
    this.indices = indices;
  }

  @Override
  public long getCount(IQueryBuilder queryBuilder) throws IOException
  {
    CountResponse count = ElasticServiceDAS.instance().count(((QueryBuilder) queryBuilder).getQuery(), indices);
    return count.getCount();
  }

  @Override
  public List<Map<String, Object>> getSearchDocuments(ISearchBuilder searchBuilder) throws IOException
  {
    SearchHit[] hits = ElasticServiceDAS.instance().search(((SearchBuilder) searchBuilder).getSource(), indices);
    List<Map<String, Object>> documents = new ArrayList<>();
    for (SearchHit hit : hits) {
      documents.add(hit.getSourceAsMap());
    }
    return documents;
  }
  
  @Override
  public List<String> getHitIds(ISearchBuilder searchBuilder) throws IOException
  {
    SearchHit[] hits = ElasticServiceDAS.instance().search(((SearchBuilder) searchBuilder).getSource(), indices);
    List<String> ids = new ArrayList<>();
    for (SearchHit hit : hits) 
    {
      ids.add(hit.getId());
    }
    
    return ids;
  }

  @Override
  public List<String> getHitIdsAndFillHighlightsMap(ISearchBuilder searchBuilder, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws IOException
  {
    SearchHit[] hits = ElasticServiceDAS.instance().search(((SearchBuilder) searchBuilder).getSource(), indices);
    List<String> ids = new ArrayList<>();
    for (SearchHit hit : hits) {
      ids.add(hit.getId());
      fillHighlightsMap(hit, idVsHighlightsMap);
    }
    
    return ids;
  }

  private void fillHighlightsMap(SearchHit hit, Map<String, List<Map<String, Object>>> idVsHighlightsMap)
  {
    Map<String, SearchHits> innerHitsMap = hit.getInnerHits();
    if (innerHitsMap != null) 
    {
      List<Map<String, Object>> highlightList = new ArrayList<>();

      for (Map.Entry<String, SearchHits> entry : innerHitsMap.entrySet()) 
      {
        SearchHits innerHits = entry.getValue();
        if (innerHits != null) 
        {

          for (SearchHit innerHit : innerHits) 
          {
            Map<String, HighlightField> highlightField = innerHit.getHighlightFields();
            
            for (Map.Entry<String, HighlightField> highlightFieldEntry : highlightField.entrySet()) 
            {
              Map<String, Object> highlightReturn = new HashMap<String, Object>();
              String key = highlightFieldEntry.getKey();
              String[] split = key.split("\\.");
              String propertyId = key.endsWith("rawlowercase") || key.endsWith("raw") ? split[split.length - 2] : split[split.length - 1];
              
              Text[] fragments = highlightFieldEntry.getValue().fragments();
              List<String> valuesList = new ArrayList<>();

              if(split[1].equals("attribute")) 
              {
                highlightReturn.put("type", "attribute");
                String[] split2 = propertyId.split("_", 2);
                propertyId = split2[split2.length -1];
                valuesList.add(fragments[0].toString());
              }
              else
              {
                highlightReturn.put("type", "tag");
                
                for (Text fragment : fragments) 
                {
                  String value = fragment.toString().replace("<em>", "").replace("</em>", "");
                  valuesList.add(value);
                }
              }
              
              highlightReturn.put("id", propertyId);
              highlightReturn.put("code", propertyId);
              highlightReturn.put("values", valuesList);
              highlightList.add(highlightReturn);
            }
          }
        }
      }
      idVsHighlightsMap.put(hit.getId(), highlightList);
    }
  }

  @Override
  public Map<String, Long> getAggregation(ISearchBuilder searchBuilder, IAggregator aggregator) throws IOException
  {
    Aggregator yo = (Aggregator) aggregator;
    Aggregations x = ElasticServiceDAS.instance().aggregate(((SearchBuilder) searchBuilder).getSource(), indices);
    return yo.parseResult(x.getAsMap());
  }

  @Override
  public List<Long> executeMultiCount(List<IQueryBuilder> queries) throws IOException
  {
    MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

    for(IQueryBuilder query : queries){
      SearchRequest request = new SearchRequest(indices);
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(((QueryBuilder)query).getQuery());
      searchSourceBuilder.from(0);
      searchSourceBuilder.size(0);
      searchSourceBuilder.fetchSource(false);
      searchSourceBuilder.trackScores(false);
      request.source(searchSourceBuilder);
      multiSearchRequest.add(request);
    }

    MultiSearchResponse multiResponse = ElasticServiceDAS.instance().multiSearch(multiSearchRequest);
    MultiSearchResponse.Item[] responses = multiResponse.getResponses();
    List<Long> counts = new ArrayList<>();
    for (MultiSearchResponse.Item response : responses) {
      counts.add(response.getResponse().getHits().getTotalHits().value);
    }
    return counts;
  }

  @Override
  public Map<String, Map<String, Long>> getMultipleAggregation(ISearchBuilder searchBuilder, Map<String, IAggregator> aggregators)
      throws IOException
  {
    Map<String, Map<String, Long>> propertyCodeVsCounts = new HashMap<>();
    if(aggregators.isEmpty()) {
      return propertyCodeVsCounts;
    }
    Aggregations x = ElasticServiceDAS.instance().aggregate(((SearchBuilder) searchBuilder).getSource(), indices);
    Map<String, Aggregation> asMap = x.getAsMap();
    for (Map.Entry<String,IAggregator> entry : aggregators.entrySet()) {
      Aggregator aggregator = (Aggregator) entry.getValue();
      AggregationType aggregationType = aggregator.getRequest().getAggregationType();
      String key = aggregationType.equals(AggregationType.byRange)? entry.getKey() + "_" + RangeAggName.nestedRangeAgg:entry.getKey() + "_" + PropertyAggName.nestedAgg;
      Map<String,Aggregation> currentAgg = new HashMap<>();
      currentAgg.put(key, asMap.get(key));
      Map<String, Long> counts = aggregator.parseResult(currentAgg);
      propertyCodeVsCounts.put(entry.getKey(), counts);
      
    }
    return propertyCodeVsCounts;
  }
}
