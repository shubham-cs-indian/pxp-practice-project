package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDashboardSummaryModel extends IModel {
  
  public static final String NAME = "label";
  public static final String URL  = "url";
  public static final String ICON = "icon";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getUrl();
  
  public void setUrl(String url);
  
  public String getIcon();
  
  public void setIcon(String icon);
}
