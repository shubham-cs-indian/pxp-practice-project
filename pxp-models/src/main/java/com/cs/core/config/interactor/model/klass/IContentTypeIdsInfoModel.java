package com.cs.core.config.interactor.model.klass;

public interface IContentTypeIdsInfoModel extends ITypesListModel {
  
  public static final String CONTENT_ID = "contentId";
  public static final String BASETYPE   = "baseType";
  public static final String NAME       = "name";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getName();
  
  public void setName(String name);
}
