package com.cs.core.runtime.interactor.model.translations;

import java.util.List;

public class GetStaticTranslationsForRuntimeRequestModel
    implements IGetStaticTranslationsForRuntimeRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    screens;
  protected String          language;
  
  @Override
  public List<String> getScreens()
  {
    return screens;
  }
  
  @Override
  public void setScreens(List<String> screens)
  {
    this.screens = screens;
  }
  
  @Override
  public String getLanguage()
  {
    return language;
  }
  
  @Override
  public void setLanguage(String language)
  {
    this.language = language;
  }
}
