package com.cs.core.elastic.builders;

import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.List;

public class QueryBuilder implements IQueryBuilder {

  protected List<IPropertyDTO.PropertyType> numericTypes = Arrays.asList(IPropertyDTO.PropertyType.CALCULATED,
      IPropertyDTO.PropertyType.MEASUREMENT, IPropertyDTO.PropertyType.PRICE, IPropertyDTO.PropertyType.NUMBER,
      IPropertyDTO.PropertyType.DATE);

  protected BoolQueryBuilder query;
  protected ISearchDTO       searchDTO;

  public BoolQueryBuilder getQuery()
  {
    return query;
  }

  public QueryBuilder(ISearchDTO searchDto)
  {
    this.searchDTO = searchDto;
    this.query = new BoolQueryBuilder();
  }

  protected QueryBuilder(ISearchDTO searchDto, BoolQueryBuilder query)
  {
    this.searchDTO = searchDto;
    this.query = query;
  }

  public IQueryBuilder baseFilter()
  {
    query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.catalogCode.name(), searchDTO.getCatalogCode()));
    query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.organisationCode.name(), searchDTO.getOrganizationCode()));
    if (!StringUtils.isEmpty(searchDTO.getEndpointCode())) {
      query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.endpointCode.name(), searchDTO.getEndpointCode()));
    }
    prepareIsRootFilter();
    return this;
  }

  protected void prepareIsRootFilter()
  {
    RootFilter rootFilter = searchDTO.getRootFilter();
    if(rootFilter.equals(RootFilter.TRUE)){
      query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.isRoot.name(), true));
    }
    else if(rootFilter.equals(RootFilter.FALSE)){
      query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.isRoot.name(), false));
    }
  }
  
  @Override public IQueryBuilder permissions()
  {
    return new ClassifierQueryBuilder(searchDTO, query).permissions();
  }

  @Override public IQueryBuilder classifiers()
  {
    return new ClassifierQueryBuilder(searchDTO, query).classifiers();
  }

  @Override public IQueryBuilder simpleSearch()
  {
    return new PropertyQueryBuilder(searchDTO, query).simpleSearch();
  }

  @Override public IQueryBuilder advanceSearch()
  {
    return new PropertyQueryBuilder(searchDTO, query).advanceSearch();
  }
  
  @Override
  public IQueryBuilder ruleViolationFilters()
  {
    return new RuleViolationQueryBuilder(searchDTO, query).ruleViolationFilters();
  }

  @Override
  public IQueryBuilder excludeIds()
  {
    List<Long> idsToExclude = searchDTO.getIdsToExclude();
    if (idsToExclude.isEmpty()) {
      return this;
    }
    query.mustNot(QueryBuilders.termsQuery("_id", idsToExclude));
    return this;
  }
  
  @Override public IQueryBuilder collectionFilter()
  {
    return new FlatFieldQueryBuilder(searchDTO, query).collectionFilter();
  }
  
  @Override
  public IQueryBuilder expiredFilter()
  {
    Boolean isExpiredFilter = searchDTO.getIsExpired();
    if (isExpiredFilter != null) {
      query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.isExpired.name(), isExpiredFilter));
    }
    
    return this;
  }
  
  @Override
  public IQueryBuilder duplicateFilter()
  {
    Boolean isDuplicateFilter = searchDTO.getIsDuplicate();
    if (isDuplicateFilter != null) {
      query.filter(QueryBuilders.termQuery(ISearchBuilder.Fields.isDuplicate.name(), isDuplicateFilter));
    }
    return this;
  }
  
  @Override
  public IQueryBuilder isSearchable() {
    query.filter(QueryBuilders.termQuery(IGoldenRecordBucketDTO.IS_SEARCHABLE, true));
    return this;
  }

  @Override
  public IQueryBuilder propertyExistence(IPropertyDTO propertyDTO)
  {
    PropertyQueryBuilder propertyQueryBuilder = new PropertyQueryBuilder(searchDTO, query);
    propertyQueryBuilder.checkIfPropertyExists(propertyDTO);
    return propertyQueryBuilder;
  }

  @Override
  public IQueryBuilder contextFilter()
  {
    FlatFieldQueryBuilder flatFieldQueryBuilder = new FlatFieldQueryBuilder(searchDTO, query);
    return flatFieldQueryBuilder.contextFilter();
  }

  @Override
  public IQueryBuilder parentFilter()
  {
    FlatFieldQueryBuilder flatFieldQueryBuilder = new FlatFieldQueryBuilder(searchDTO, query);
    return flatFieldQueryBuilder.parentFilter();
  }

  @Override
  public IQueryBuilder timeRange()
  {
    FlatFieldQueryBuilder flatFieldQueryBuilder = new FlatFieldQueryBuilder(searchDTO, query);
    return flatFieldQueryBuilder.timeRangeFilter();
  }
}
