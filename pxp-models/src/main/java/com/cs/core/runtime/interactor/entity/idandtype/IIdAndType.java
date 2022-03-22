package com.cs.core.runtime.interactor.entity.idandtype;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IIdAndType extends IEntity {
  
  public static final String TYPE = "type";
  
  public String getType();
  
  public void setType(String baseType);
}
