package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;

public class ConfigTaskInformationModel extends ConfigEntityInformationModel
    implements IConfigTaskInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  private String            color;
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
}
