package com.cs.core.rdbms.entity.dto;

import java.util.ArrayList;
import java.util.Collection;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IRangeAggregationDTO;


/**
 * This DTO is used for Range Query's Aggregations.
 * 
 * @author faizan.siddique
 *
 */
public class RangeAggregationDTO extends PropertyAggregationDTO implements IRangeAggregationDTO {
  
  Object baseQuery;
  Collection<String> indices = new ArrayList<String>();
  
  public RangeAggregationDTO(IPropertyDTO property, Boolean isTranslatable)
  {
    super(property, isTranslatable);
    this.setAggregationType(AggregationType.byRange);
  }

  @Override
  public Object getBaseQuery()
  {
    return baseQuery;
  }
  
  @Override
  public void setBaseQuery(Object baseQuery)
  {
    this.baseQuery = baseQuery;
  }
  
  @Override
  public Collection<String> getIndices()
  {
    return indices;
  }
  
  @Override
  public void setIndices(Collection<String> indices)
  {
    this.indices = indices;
  }
  
}
