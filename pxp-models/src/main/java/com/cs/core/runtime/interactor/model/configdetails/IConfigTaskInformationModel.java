package com.cs.core.runtime.interactor.model.configdetails;

public interface IConfigTaskInformationModel extends IConfigEntityInformationModel {
  
  public static final String COLOR = "color";
  
  public String getColor();
  
  public void setColor(String color);
}
