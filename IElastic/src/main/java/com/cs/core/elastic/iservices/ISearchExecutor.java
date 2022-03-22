package com.cs.core.elastic.iservices;

import com.cs.core.elastic.ibuilders.IAggregator;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ISearchExecutor {

  /**
   *
   * @param queryBuilder query to execute for count
   * @return count of document returned in search result
   * @throws IOException
   */
  long getCount(IQueryBuilder queryBuilder) throws IOException;

  /**
   *
   * @param searchBuilder searchSource to execute for search
   * @return list of documents retrieved from search execution.
   * @throws IOException
   */
  List<Map<String, Object>> getSearchDocuments(ISearchBuilder searchBuilder) throws IOException;

  /**
   *
   * @param searchBuilder searchSource to execute for search
   * @return  list of ids retrieved from search execution (use when number of documents are paginated or limited).
   * @throws IOException
   */
  List<String> getHitIds(ISearchBuilder searchBuilder) throws IOException;
  
  /**
   * @param searchBuilder searchSource to execute for search
   * @param idVsHighlightsMap map to be filled with highlights obtained from hits.
   * @return  list of ids retrieved from search execution (use when number of documents are paginated or limited).
   * @throws IOException
   */
  List<String> getHitIdsAndFillHighlightsMap(ISearchBuilder searchBuilder, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws IOException;

  Map<String, Long> getAggregation(ISearchBuilder searchBuilder, IAggregator aggregator) throws IOException;

  Map<String,Map<String, Long>> getMultipleAggregation(ISearchBuilder searchBuilder, Map<String,IAggregator> aggregator) throws IOException;

  public List<Long> executeMultiCount(List<IQueryBuilder> queries) throws IOException;
  
}
