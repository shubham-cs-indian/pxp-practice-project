package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface INomenclatureElementModel extends IModel {
  
  public static final String ID       = "id";
  public static final String NAME     = "name";
  public static final String PARENTID = "parentId";
  public static final String LEVEL    = "level";
  
  public String getId();
  
  public void setId(String id);
  
  public String getName();
  
  public void setName(String name);
  
  public String getParentId();
  
  public void setParentId(String parentid);
  
  public int getLevel();
  
  public void setLevel(int level);
}
