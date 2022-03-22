package com.cs.core.runtime.interactor.model.klassinstance;

public interface ITypesTaxonomiesBaseTypeModel extends ITypesTaxonomiesModel {
  
  public static final String ID        = "id";
  public static final String BASE_TYPE = "baseType";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getId();
  
  public void setId(String id);
}
