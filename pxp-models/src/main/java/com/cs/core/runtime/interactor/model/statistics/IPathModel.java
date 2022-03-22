package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPathModel extends IModel {
  
  public static final String TYPE_ID   = "typeId";
  public static final String TYPE      = "type";
  public static final String PARENT_ID = "parentId";
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public String getType();
  
  public void setType(String type);
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
