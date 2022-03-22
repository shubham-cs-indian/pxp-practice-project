package com.cs.core.runtime.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetStaticTranslationsForRuntimeRequestModel extends IModel {
  
  public static final String SCREENS  = "screens";
  public static final String LANGUAGE = "language";
  
  public List<String> getScreens();
  
  public void setScreens(List<String> screens);
  
  public String getLanguage();
  
  public void setLanguage(String language);
}
