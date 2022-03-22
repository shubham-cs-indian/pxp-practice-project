package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdAndLanguageIdModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LANGUAGE_ID = "languageId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLanguageId();
  
  public void setLanguageId(String languageId);
}
