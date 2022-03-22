package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetLanguagesRequestModel extends IModel {
  
  public static String IS_GET_DATA_LANGUAGES = "isGetDataLanguages";
  public static String IS_GET_UI_LANGUAGES   = "isGetUILanguages";
  public static String DATA_LANGUAGE         = "dataLanguage";
  public static String UI_LANGUAGE           = "uiLanguage";
  
  public Boolean getIsGetDataLanguages();
  
  public void setIsGetDataLanguages(Boolean isGetDataLanguages);
  
  public Boolean getIsGetUILanguages();
  
  public void setIsGetUILanguages(Boolean isGetUILanguages);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
}
