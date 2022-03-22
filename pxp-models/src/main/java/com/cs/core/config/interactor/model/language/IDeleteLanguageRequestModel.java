package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteLanguageRequestModel extends IModel {
  
  public static String IDS           = "ids";
  public static String DATA_LANGUAGE = "dataLanguage";
  public static String UI_LANGUAGE   = "uiLanguage";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
}
