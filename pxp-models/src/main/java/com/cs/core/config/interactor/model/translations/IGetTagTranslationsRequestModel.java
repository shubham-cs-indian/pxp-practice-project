package com.cs.core.config.interactor.model.translations;

public interface IGetTagTranslationsRequestModel extends IGetTranslationsRequestModel {
  
  public static final String PARENT_ID = "parentId";
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
