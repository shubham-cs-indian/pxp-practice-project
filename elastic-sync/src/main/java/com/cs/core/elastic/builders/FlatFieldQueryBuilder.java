package com.cs.core.elastic.builders;

import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.IVariantTableViewDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class FlatFieldQueryBuilder extends QueryBuilder {

  public FlatFieldQueryBuilder(ISearchDTO searchDto, BoolQueryBuilder query)
  {
    super(searchDto, query);
  }
  
  @Override
  public IQueryBuilder collectionFilter()
  {
    if (searchDTO.getCollectionFilter() != null) {
      String collectionId = searchDTO.getCollectionFilter().getCollectionId();
      if (collectionId != null) {
        if (!searchDTO.getCollectionFilter().isQuickList()) {
          query.filter(QueryBuilders.termQuery(Fields.collectionIds.name(), collectionId));
        }
        else {
          query.mustNot(QueryBuilders.termsQuery(Fields.collectionIds.name(), collectionId));
        }
      }
    }
    return this;
  }

  public IQueryBuilder contextFilter()
  {
    if(searchDTO instanceof IVariantTableViewDTO){
      String contextId = ((IVariantTableViewDTO)searchDTO).getContextId();
      if (contextId != null) {
        query.filter(QueryBuilders.termQuery(Fields.contextID.name(), contextId));
      }
    }
    return this;
  }

  public IQueryBuilder parentFilter()
  {
    if(searchDTO instanceof IVariantTableViewDTO){
      Long parentIID = ((IVariantTableViewDTO)searchDTO).getParentIID();
      if (parentIID != null) {
        query.filter(QueryBuilders.termQuery(Fields.parentIID.name(), parentIID));
      }
    }
    return this;
  }

  public IQueryBuilder timeRangeFilter()
  {
    if (searchDTO instanceof IVariantTableViewDTO) {
      Long startTime = ((IVariantTableViewDTO) searchDTO).getStartTime();
      Long endTime = ((IVariantTableViewDTO) searchDTO).getEndTime();
      if(startTime == null || endTime == null){
        return this;
      }
      BoolQueryBuilder timeRangeQuery = QueryBuilders.boolQuery();
      query.filter(timeRangeQuery);
      timeRangeQuery.filter(QueryBuilders.rangeQuery(Fields.contextStartTime.name()).lte(endTime));
      timeRangeQuery.filter(QueryBuilders.rangeQuery(Fields.contextEndTime.name()).gte(startTime));
    }
    return this;
  }
}
