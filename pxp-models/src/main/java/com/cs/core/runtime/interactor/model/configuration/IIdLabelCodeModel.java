package com.cs.core.runtime.interactor.model.configuration;

public interface IIdLabelCodeModel extends IModel {
  
  public static final String ID    = "id";
  public static final String LABEL = "label";
  public static final String CODE  = "code";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
}
