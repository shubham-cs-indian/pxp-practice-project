package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAddedTabModel extends IModel {
  
  public static final String ID               = "id";
  public static final String LABEL            = "label";
  public static final String CODE             = "code";
  public static final String IS_NEWLY_CREATED = "isNewlyCreated";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Boolean getIsNewlyCreated();
  
  public void setIsNewlyCreated(Boolean isNewlyCreated);
}
