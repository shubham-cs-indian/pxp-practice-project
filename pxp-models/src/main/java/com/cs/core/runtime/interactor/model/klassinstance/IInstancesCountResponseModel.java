package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IInstancesCountResponseModel extends IModel {
  
  String TYPE_ID_VS_INSTANCES_COUNT = "typeIdVsInstancesCount";
  
  public Map<String, Long> getTypeIdVsInstancesCount();
  
  public void setTypeIdVsInstancesCount(Map<String, Long> typeIdVsInstancesCount);
}
