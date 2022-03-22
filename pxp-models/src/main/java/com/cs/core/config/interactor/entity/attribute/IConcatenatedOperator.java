package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IConcatenatedOperator extends IConfigEntity {
  
  public static final String TYPE  = "type";
  public static final String ORDER = "order";
  
  public String getType();
  
  public void setType(String type);
  
  public Integer getOrder();
  
  public void setOrder(Integer order);
}
