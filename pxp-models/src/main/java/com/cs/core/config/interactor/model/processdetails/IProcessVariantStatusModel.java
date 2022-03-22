package com.cs.core.config.interactor.model.processdetails;

public interface IProcessVariantStatusModel extends IProcessKlassInstanceStatusModel {
  
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String PARENT_ID         = "parentId";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
