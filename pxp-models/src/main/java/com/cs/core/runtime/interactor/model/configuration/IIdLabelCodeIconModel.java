package com.cs.core.runtime.interactor.model.configuration;

public interface IIdLabelCodeIconModel extends IIdLabelCodeModel {
  
  public static final String ICON    = "icon";
  public static final String ICON_KEY = "iconKey";
  
  public String getIcon();
  public void setIcon(String icon);
  
  public String getIconKey();
  public void setIconKey(String iconKey);
}
