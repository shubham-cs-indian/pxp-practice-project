package com.cs.core.config.interactor.model.klass;

public interface IReferencedSectionForSwitchTypeModel extends IReferencedSectionElementModel {
  
  public static final String SOURCE_ID   = "sourceId";
  public static final String SOURCE_TYPE = "sourceType";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public String getSourceType();
  
  public void setSourceType(String sourceType);
}
