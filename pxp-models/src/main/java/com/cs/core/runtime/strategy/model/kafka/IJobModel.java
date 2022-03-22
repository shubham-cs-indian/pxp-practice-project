package com.cs.core.runtime.strategy.model.kafka;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJobModel extends IModel {
  
  String getId();
  
  void setId(String id);
  
  String getTargetBeanName();
  
  void setTargetBeanName(String targetBeanName);
  
  IQueuedRequestModel getRequestModel();
  
  void setRequestModel(IQueuedRequestModel requestModel);
  
  Integer getProducerId();
  
  void setProducerId(Integer producerId);
}
