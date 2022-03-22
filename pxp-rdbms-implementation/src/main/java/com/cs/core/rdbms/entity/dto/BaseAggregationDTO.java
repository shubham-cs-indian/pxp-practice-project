package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;

public class BaseAggregationDTO implements IAggregationRequestDTO {

  private AggregationType aggregationType;
  private int size;

  @Override public AggregationType getAggregationType()
  {
    return aggregationType;
  }

  @Override
  public void setAggregationType(AggregationType aggregationType)
  {
    this.aggregationType = aggregationType;
  }

  @Override
  public int getSize()
  {
    return size;
  }

  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
}
