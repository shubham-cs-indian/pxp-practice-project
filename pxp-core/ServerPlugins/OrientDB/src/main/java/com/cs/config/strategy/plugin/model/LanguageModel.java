package com.cs.config.strategy.plugin.model;

public class LanguageModel implements ILanguageModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          uiLanguage;
  protected String          dataLanguage;
  
  @Override
  public String getUiLanguage()
  {
    return uiLanguage;
  }
  
  @Override
  public void setUiLanguage(String uiLanguage)
  {
    this.uiLanguage = uiLanguage;
  }
  
  @Override
  public String getDataLanguage()
  {
    return dataLanguage;
  }
  
  @Override
  public void setDataLanguage(String dataLanguage)
  {
    this.dataLanguage = dataLanguage;
  }
}
