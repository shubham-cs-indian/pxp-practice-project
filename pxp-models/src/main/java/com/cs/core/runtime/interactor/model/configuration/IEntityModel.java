package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IEntityModel extends IModel, IEntity {
  
  @JsonIgnore
  public IEntity getEntity();
}
