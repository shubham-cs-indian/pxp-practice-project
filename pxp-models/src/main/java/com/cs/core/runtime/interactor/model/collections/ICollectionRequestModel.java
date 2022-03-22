package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICollectionRequestModel extends IModel {
  
  public static final String ID   = "id";
  public static final String FROM = "from";
  public static final String SIZE = "size";
  
  public String getId();
  
  public void setId(String id);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public int getSize();
  
  public void setSize(int size);
}
