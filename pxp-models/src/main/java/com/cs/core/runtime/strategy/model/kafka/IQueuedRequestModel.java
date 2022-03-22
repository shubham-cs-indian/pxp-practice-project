package com.cs.core.runtime.strategy.model.kafka;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IQueuedRequestModel extends IModel {
  
  String getJobId();
  
  void setJobId(String jobId);
  
  IModel getModel();
  
  void setModel(IModel model);
}
