package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IPosition extends IConfigEntity {
  
  public static final String X = "x";
  public static final String Y = "y";
  
  public Integer getX();
  
  public void setX(Integer x);
  
  public Integer getY();
  
  public void setY(Integer y);
}
