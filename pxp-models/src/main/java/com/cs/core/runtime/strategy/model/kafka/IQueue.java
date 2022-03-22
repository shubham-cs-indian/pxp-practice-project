package com.cs.core.runtime.strategy.model.kafka;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IQueue<P extends IModel, R extends IModel> {
  
  Integer getProducerId() throws InterruptedException;
  
  R queueJob(IJobModel jobObject, String entityId) throws Exception;
}
