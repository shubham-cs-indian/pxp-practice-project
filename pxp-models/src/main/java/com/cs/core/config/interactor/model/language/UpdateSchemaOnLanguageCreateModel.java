package com.cs.core.config.interactor.model.language;

public class UpdateSchemaOnLanguageCreateModel implements IUpdateSchemaOnLangaugeCreateModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          languageCode;
  
  public UpdateSchemaOnLanguageCreateModel()
  {
  }
  
  public UpdateSchemaOnLanguageCreateModel(String languageCode)
  {
    this.languageCode = languageCode;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languagecode)
  {
    this.languageCode = languagecode;
  }
}
