package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPaginationInfoSortModel extends IModel {
  
  public static final String FROM       = "from";
  public static final String SIZE       = "size";
  
  public void setFrom(Long from);
  public Long getFrom();
  
  public void setSize(Long size);
  public Long getSize();
  
}
