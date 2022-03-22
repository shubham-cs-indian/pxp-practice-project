package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUserLanguageModel extends IModel {
  
  public static final String PREFERRED_DATA_LANGUAGE = "preferredDataLanguage";
  public static final String PREFERRED_UI_LANGUAGE   = "preferredUILanguage";
  public static final String IS_LANGUAGE_CHANGED     = "isLanguageChanged";
  
  public Boolean getIsLanguageChanged();
  public void setIsLanguageChanged(Boolean isLanguageChanged);
  
  public String getPreferredDataLanguage();
  public void setPreferredDataLanguage(String preferredDataLanguage);
  
  public String getPreferredUILanguage();
  public void setPreferredUILanguage(String preferredUILanguage);
}
