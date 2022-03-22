package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.Serializable;

public interface IConfigEntityInformationModel extends IModel, Serializable {
  
  public static final String ID       = "id";
  public static final String LABEL    = "label";
  public static final String TYPE     = "type";
  public static final String ICON     = "icon";
  public static final String ICON_KEY = "iconKey";
  
  public static final String CODE     = "code";
  public static final String IID      = "iid";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
public String getIconKey();
  
  public void setIconKey(String iconKey);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Long getIid();
  
  public void setIid(Long iid);
}
