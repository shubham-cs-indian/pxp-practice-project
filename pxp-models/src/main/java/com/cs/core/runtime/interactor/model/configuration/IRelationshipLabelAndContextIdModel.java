package com.cs.core.runtime.interactor.model.configuration;

public interface IRelationshipLabelAndContextIdModel extends IModel {
  
  public static String LABEL      = "label";
  public static String CONTEXT_ID = "contextId";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getContextId();
  
  public void setContextId(String contextId);
}
