package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IPropertyCollectionElement extends IConfigEntity {
  
  public static final String TYPE     = "type";
  public static final String INDEX    = "index";
  public static final String POSITION = "position";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public Integer getIndex();
  
  public void setIndex(Integer index);
}
