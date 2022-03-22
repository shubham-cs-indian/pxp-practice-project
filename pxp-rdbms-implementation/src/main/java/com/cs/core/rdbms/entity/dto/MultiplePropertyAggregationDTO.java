package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;

import java.util.HashMap;
import java.util.Map;

public class MultiplePropertyAggregationDTO implements IMultiplePropertyAggregationDTO {

  private Map<String, IPropertyAggregationDTO> aggregationRequests = new HashMap<>();

  @Override
  public Map<String, IPropertyAggregationDTO> getAggregationRequests()
  {
    return aggregationRequests;
  }

  @Override
  public void setAggregationRequests(Map<String, IPropertyAggregationDTO> aggregationRequests)
  {
    this.aggregationRequests = aggregationRequests;
  }
}
