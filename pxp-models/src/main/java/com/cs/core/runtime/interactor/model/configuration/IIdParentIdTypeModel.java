package com.cs.core.runtime.interactor.model.configuration;

public interface IIdParentIdTypeModel extends IModel {
  
  public static final String ID         = "id";
  public static final String PARENT_ID  = "parentId";
  public static final String KLASS_TYPE = "klassType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
}
