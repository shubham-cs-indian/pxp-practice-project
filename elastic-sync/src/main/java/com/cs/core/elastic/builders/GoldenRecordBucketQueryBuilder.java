package com.cs.core.elastic.builders;

import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTO;

public class GoldenRecordBucketQueryBuilder extends QueryBuilder implements IQueryBuilder{

  public GoldenRecordBucketQueryBuilder(ISearchDTO searchDto)
  {
    super(searchDto);
  }
  
  @Override
  protected void prepareIsRootFilter()
  {
    //DO Nothing
  }
}
