package com.cs.core.runtime.interactor.entity.idandtype;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IIdAndBaseType extends IEntity {
  
  public static final String BASE_TYPE = "baseType";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
