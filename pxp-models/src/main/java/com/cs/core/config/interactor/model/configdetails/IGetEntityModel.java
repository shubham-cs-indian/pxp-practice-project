package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEntityModel extends IModel {
  
  public static final String ID    = "id";
  public static final String LABEL = "label";
  public static final String TYPE  = "type";
  public static final String ICON  = "icon";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public String getIcon();
  
  public void setIcon(String icon);
}
