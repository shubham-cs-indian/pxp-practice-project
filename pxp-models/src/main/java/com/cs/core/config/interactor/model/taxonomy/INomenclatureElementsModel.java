package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface INomenclatureElementsModel extends IModel {
  
  public static final String ID        = "id";
  public static final String LABEL     = "label";
  public static final String PARENT_ID = "parentId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
