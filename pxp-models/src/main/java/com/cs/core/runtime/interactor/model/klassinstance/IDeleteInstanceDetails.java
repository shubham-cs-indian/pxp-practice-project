package com.cs.core.runtime.interactor.model.klassinstance;

public interface IDeleteInstanceDetails {
  
  public static final String ID              = "id";
  public static final String CLASSIFIER_CODE = "classifierCode";
  public static final String BASETYPE        = "baseType";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getId();
  
  public void setId(String id);
  
  public String getClassifierCode();
  
  public void setClassifierCode(String classifierCode);
}
