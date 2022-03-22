package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISortModel extends IModel {
  
  public String getSortField();
  
  public void setSortField(String sortField);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public Boolean getIsNumeric();
  
  public void setIsNumeric(Boolean isNumeric);
}
