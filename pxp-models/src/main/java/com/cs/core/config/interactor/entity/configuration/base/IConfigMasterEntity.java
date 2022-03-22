package com.cs.core.config.interactor.entity.configuration.base;

public interface IConfigMasterEntity extends IConfigEntity {
  
  public static final String LABEL    = "label";
  public static final String ICON     = "icon";
  public static final String ICON_KEY = "iconKey";
  public static final String TYPE     = "type";
  public static final String CODE     = "code";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getIconKey();
  
  public void setIconKey(String icon);
  
  public String getType();
  
  public void setType(String type);
}
