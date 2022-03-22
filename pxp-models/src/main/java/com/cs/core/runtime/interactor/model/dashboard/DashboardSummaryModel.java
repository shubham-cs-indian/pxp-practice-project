package com.cs.core.runtime.interactor.model.dashboard;

public class DashboardSummaryModel implements IDashboardSummaryModel {
  
  protected String label;
  protected String url;
  protected String icon;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getUrl()
  {
    return url;
  }
  
  @Override
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
}
