package com.cs.core.config.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICollectionNodeModel extends IModel {
  
  public static final String ID        = "id";
  public static final String PARENT_ID = "parentId";
  public static final String LABEL     = "label";
  
  public String getId();
  
  public void setId(String id);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getLabel();
  
  public void setLabel(String label);
}
