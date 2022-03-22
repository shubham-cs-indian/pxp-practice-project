package com.cs.core.config.interactor.model.language;

public interface ICreateLanguageModel extends ILanguageModel {
  
  public static final String PARENT_ID = "parentId";
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
