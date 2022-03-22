package com.cs.config.strategy.plugin.model;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ILanguageModel extends IModel {
  
  public static final String UI_LANGUAGE   = "uiLanguage";
  public static final String DATA_LANGUAGE = "dataLanguage";
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
}
