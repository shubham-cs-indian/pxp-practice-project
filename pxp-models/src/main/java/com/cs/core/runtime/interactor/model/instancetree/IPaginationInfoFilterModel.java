package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPaginationInfoFilterModel extends IModel {
  
  public static final String SIZE       = "size";
  
  public void setSize(Integer size);
  public Integer getSize();
}
