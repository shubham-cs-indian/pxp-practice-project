package com.cs.core.runtime.interactor.entity.configuration.base;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface ISortEntity extends IEntity {
  
  public String getSortField();
  
  public void setSortField(String sortField);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public Boolean getIsNumeric();
  
  public void setIsNumeric(Boolean isNumeric);
}
