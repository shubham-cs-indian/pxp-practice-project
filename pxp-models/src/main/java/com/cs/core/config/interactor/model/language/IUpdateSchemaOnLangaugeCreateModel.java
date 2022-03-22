package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUpdateSchemaOnLangaugeCreateModel extends IModel {
  
  public static final String LANGUAGE_CODE = "languageCode";
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languagecode);
}
