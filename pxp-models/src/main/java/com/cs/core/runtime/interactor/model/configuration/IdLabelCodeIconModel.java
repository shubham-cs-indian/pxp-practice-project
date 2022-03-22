package com.cs.core.runtime.interactor.model.configuration;

public class IdLabelCodeIconModel extends IdLabelCodeModel implements IIdLabelCodeIconModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          icon;
  protected String          iconKey;

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
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
}
