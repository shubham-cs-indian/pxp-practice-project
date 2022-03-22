package com.cs.core.runtime.interactor.model.configuration;

public interface IIdAndBaseTypeModel extends IModel {
  
  public static String ID        = "id";
  public static String BASE_TYPE = "baseType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
