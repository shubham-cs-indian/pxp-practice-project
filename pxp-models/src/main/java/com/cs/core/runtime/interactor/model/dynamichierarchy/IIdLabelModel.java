package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdLabelModel extends IModel {
  
  public static final String ID    = "id";
  public static final String LABEL = "label";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
}
