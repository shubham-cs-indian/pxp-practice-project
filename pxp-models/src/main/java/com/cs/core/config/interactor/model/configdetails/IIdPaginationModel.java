package com.cs.core.config.interactor.model.configdetails;


import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdPaginationModel extends IModel {
  
  public static final String ID = "id";
  public static final String FROM = "from";
  public static final String SIZE = "size";
  
  public String getId();
  public void setId(String id);
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
}
