package com.cs.core.config.interactor.model.transfer;

public interface IEmbeddedVariantDataModel {
  
  public static final String ID = "id";
  
  public String getId();
  
  public void setId(String id);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
}
