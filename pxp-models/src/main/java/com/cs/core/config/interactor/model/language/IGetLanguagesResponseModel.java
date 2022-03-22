package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetLanguagesResponseModel extends IModel {
  
  public static String DATA_LANGUAGES   = "dataLanguages";
  public static String UI_LANGUAGES     = "userInterfaceLanguages";
  public static String DEFAULT_LANGUAGE = "defaultLanguage";
  
  public List<IGetLanguagesInfoModel> getDataLanguages();
  
  public void setDataLanguages(List<IGetLanguagesInfoModel> dataLanguages);
  
  public List<IGetLanguagesInfoModel> getUserInterfaceLanguages();
  
  public void setUserInterfaceLanguages(List<IGetLanguagesInfoModel> dataLanguages);
  
  public String getDefaultLanguage();
  
  public void setDefaultLanguage(String defaultLanguage);
}
