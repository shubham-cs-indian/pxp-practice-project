package com.cs.core.elastic.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.builders.Aggregator;
import com.cs.core.elastic.builders.QueryBuilder;
import com.cs.core.elastic.builders.SearchBuilder;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.dto.SearchResultDTO;
import com.cs.core.elastic.ibuilders.IAggregator;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.elastic.iservices.ISearchExecutor;
import com.cs.core.elastic.services.SearchExecutor;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.AggregationResultDTO;
import com.cs.core.rdbms.entity.dto.MultiplePropertyAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationDTO;
import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;
import com.cs.core.rdbms.entity.idto.IRangeAggregationDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.rsearch.dto.ValueCountDTO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;

public class SearchDAO implements ISearchDAO {

  protected final ISearchDTO       entity;
  private final Collection<String> indices;
  
  public SearchDAO(ISearchDTO entity)
  {
    this.entity = entity;
    indices = entity.getIsArchivePortal()? Index.getArchiveIndices(entity.getBaseTypes()) : Index.getIndices(entity.getBaseTypes());
  }
  
  public SearchDAO(ISearchDTO entity, Collection<String> indices)
  {
    this.entity = entity;
    this.indices = indices;
  }

  protected IQueryBuilder prepareBasicQuery()
  {
    return new QueryBuilder(entity)
        .baseFilter()
        .classifiers()
        .permissions()
        .simpleSearch()
        .advanceSearch()
        .ruleViolationFilters()
        .excludeIds()
        .collectionFilter()
        .expiredFilter()
        .duplicateFilter();
  }
  
  private ISearchResultDTO executeSearch(IQueryBuilder queryBuilder, ISearchBuilder searchBuilder, ISearchExecutor executor)
      throws IOException
  {
    ISearchResultDTO searchResult = new SearchResultDTO();
    searchResult.setTotalCount(executor.getCount(queryBuilder));
    List<String> ids = new ArrayList<>();
    
    if(entity.getXrayEnabled())
    {
      Map<String, List<Map<String, Object>>> idVsHighlightsMap = new HashMap<>();
      ids = executor.getHitIdsAndFillHighlightsMap(searchBuilder, idVsHighlightsMap);
      searchResult.setHighlightsMap(idVsHighlightsMap);
    }
    else
    {
      ids = executor.getHitIds(searchBuilder);
    }
    searchResult.setBaseEntityIIDs(ids);
    return searchResult;
  }

  private ISearchBuilder buildSearch(IQueryBuilder queryBuilder)
  {
    ISearchBuilder searchBuilder = new SearchBuilder(entity.getLocaleId());
    searchBuilder.order(entity.getOrder());
    searchBuilder.paginate(entity.getFrom(), entity.getSize());
    searchBuilder.shouldFetchSource(false);
    searchBuilder.query(queryBuilder);
    return searchBuilder;
  }
  @Override
  public ISearchResultDTO search() throws Exception
  {
    IQueryBuilder queryBuilder = prepareBasicQuery();

    ISearchResultDTO searchResult = performSearch(queryBuilder);

    return searchResult;
  }

  protected ISearchResultDTO performSearch(IQueryBuilder queryBuilder) throws IOException
  {
    ISearchBuilder searchBuilder = buildSearch(queryBuilder);

    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[] {}));

    ISearchResultDTO searchResult = executeSearch(queryBuilder, searchBuilder, executor);
    return searchResult;
  }

  @Override
  public ISearchResultDTO tableView() throws Exception
  {
    IQueryBuilder queryBuilder = new QueryBuilder(entity)
         .parentFilter()
         .contextFilter()
         .baseFilter()
         .classifiers()
         .permissions()
         .timeRange()
         .advanceSearch();

    ISearchBuilder searchBuilder = buildSearch(queryBuilder);
    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[]{}));
    return executeSearch(queryBuilder, searchBuilder, executor);
  }

  @Override
  public IAggregationResultDTO aggregation(IAggregationRequestDTO request) throws IOException
  {
    IQueryBuilder queryBuilder = prepareBasicQuery();

    ISearchBuilder searchBuilder = new SearchBuilder(entity.getLocaleId());
    searchBuilder.paginate(0, 0);
    searchBuilder.shouldFetchSource(false);
    searchBuilder.query(queryBuilder);
    
    if(request instanceof IRangeAggregationDTO) {
      IRangeAggregationDTO rangeAggDTO = (IRangeAggregationDTO) request; 
      rangeAggDTO.setBaseQuery(((QueryBuilder)queryBuilder).getQuery());
      rangeAggDTO.setIndices(indices);
    }
    
    Aggregator aggregator = new Aggregator(request, entity.getLocaleId());
    searchBuilder.aggregate(aggregator.aggregate());


    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[]{}));
    Map<String, Long> aggregation = executor.getAggregation(searchBuilder, aggregator);
    IAggregationResultDTO result = new AggregationResultDTO();
    result.setCount(aggregation);
    return result;
  }

  @Override
  public List<String> findFilterable(List<IPropertyDTO> properties) throws IOException
  {
    Collection<String> indices = entity.getIsArchivePortal()? Index.getArchiveIndices(entity.getBaseTypes()) : Index.getIndices(entity.getBaseTypes());

    List<IQueryBuilder> queryBuilders = new ArrayList<>();
    for(IPropertyDTO property : properties) {
      QueryBuilder queryBuilder = (QueryBuilder) prepareBasicQuery();
      queryBuilders.add(queryBuilder.propertyExistence(property));
    }
    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[]{}));
    List<Long> counts = executor.executeMultiCount(queryBuilders);

    List<String> returnList = new ArrayList<>();
    int countIndex = 0;
    for(Long count : counts){
      if(count > 0){
        returnList.add(properties.get(countIndex).getPropertyCode());
      }
      countIndex = countIndex + 1;
    }
    return returnList;
  }

  @Override
  public IMultiplePropertyAggregationResultDTO multiplePropertyAggregation(IMultiplePropertyAggregationDTO request)
      throws IOException
  {
    IQueryBuilder queryBuilder = new QueryBuilder(entity)
        .parentFilter()
        .contextFilter()
        .baseFilter()
        .classifiers()
        .permissions()
        .timeRange()
        .advanceSearch();

    ISearchBuilder searchBuilder = new SearchBuilder(entity.getLocaleId());
    searchBuilder.paginate(0, 0);
    searchBuilder.shouldFetchSource(false);
    searchBuilder.query(queryBuilder);

    Map<String, IPropertyAggregationDTO> aggregationRequests = request.getAggregationRequests();

    Map<String, IAggregator> propertyCodeVsAggregators = new HashMap<>();
    for (Map.Entry<String, IPropertyAggregationDTO> aggregationRequest : aggregationRequests.entrySet()) {
      Aggregator aggregator = getAggregator(aggregationRequest.getValue(), (QueryBuilder) queryBuilder);
      propertyCodeVsAggregators.put(aggregationRequest.getKey(), aggregator);
    }

    for (Map.Entry<String, IAggregator> aggregator : propertyCodeVsAggregators.entrySet()) {
      searchBuilder.aggregate(aggregator.getValue().aggregate());
    }

    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[] {}));
    Map<String, Map<String, Long>> aggregations = executor.getMultipleAggregation(searchBuilder, propertyCodeVsAggregators);

    IMultiplePropertyAggregationResultDTO result = new MultiplePropertyAggregationResultDTO();
    for (Map.Entry<String, Map<String, Long>> aggregation : aggregations.entrySet()) {
      IPropertyAggregationDTO propertyAggregationDTO = aggregationRequests.get(aggregation.getKey());
      IValueCountDTO count = new ValueCountDTO(propertyAggregationDTO.getProperty());
      count.fillValueVScount(aggregation.getValue());
      result.getCounts().add(count);
    }
    return result;
  }

  private Aggregator getAggregator(IAggregationRequestDTO request, QueryBuilder queryBuilder)
  {
    if (request instanceof IRangeAggregationDTO) {
      IRangeAggregationDTO rangeAggDTO = (IRangeAggregationDTO) request;
      rangeAggDTO.setBaseQuery(queryBuilder.getQuery());
      rangeAggDTO.setIndices(indices);
    }
    return new Aggregator(request, entity.getLocaleId());
  }

  @Override
  public Long getCountForEntityUsage() throws IOException
  {
    IQueryBuilder classifierQuery =  new QueryBuilder(entity)
        .classifiers();
    ISearchExecutor executor = new SearchExecutor(indices.toArray(new String[] {}));
    return executor.getCount(classifierQuery);
  }
}
