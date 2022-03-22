package com.cs.core.rdbms.entity.idto;

import java.util.Map;

public interface IMultiplePropertyAggregationDTO {

  public Map<String, IPropertyAggregationDTO> getAggregationRequests();
  public void setAggregationRequests(Map<String, IPropertyAggregationDTO> aggregationRequests);


}
