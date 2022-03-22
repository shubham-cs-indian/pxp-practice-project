package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public interface IEventInstanceModel extends IEventInstance, IModel {
  
}
