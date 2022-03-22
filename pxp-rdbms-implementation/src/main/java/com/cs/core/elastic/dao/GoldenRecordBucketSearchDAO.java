package com.cs.core.elastic.dao;

import java.util.Arrays;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.builders.GoldenRecordBucketQueryBuilder;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTO;

public class GoldenRecordBucketSearchDAO extends SearchDAO implements ISearchDAO{

  public GoldenRecordBucketSearchDAO(ISearchDTO entity)
  {
    super(entity, Arrays.asList(Index.goldenrecordbucketcache.name()));
  }
  
  @Override
  protected IQueryBuilder prepareBasicQuery()
  {
    return new GoldenRecordBucketQueryBuilder(entity)
        .baseFilter()
        .isSearchable()
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
  
}
