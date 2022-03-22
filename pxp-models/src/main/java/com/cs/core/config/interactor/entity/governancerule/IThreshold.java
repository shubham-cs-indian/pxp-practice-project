package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IThreshold extends IEntity {
  
  public static final String LOWER = "lower";
  public static final String UPPER = "upper";
  
  public Integer getLower();
  
  public void setLower(Integer lower);
  
  public Integer getUpper();
  
  public void setUpper(Integer upper);
}
