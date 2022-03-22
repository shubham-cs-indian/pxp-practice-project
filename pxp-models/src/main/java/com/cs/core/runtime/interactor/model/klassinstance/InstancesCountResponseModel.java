package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.HashMap;
import java.util.Map;

public class InstancesCountResponseModel implements IInstancesCountResponseModel {
  
  private static final long   serialVersionUID = 1L;
  protected Map<String, Long> typeIdVsInstancesCount;
  
  public Map<String, Long> getTypeIdVsInstancesCount()
  {
    if (typeIdVsInstancesCount == null) {
      typeIdVsInstancesCount = new HashMap<>();
    }
    return typeIdVsInstancesCount;
  }
  
  public void setTypeIdVsInstancesCount(Map<String, Long> typeIdVsInstancesCount)
  {
    this.typeIdVsInstancesCount = typeIdVsInstancesCount;
  }
}
