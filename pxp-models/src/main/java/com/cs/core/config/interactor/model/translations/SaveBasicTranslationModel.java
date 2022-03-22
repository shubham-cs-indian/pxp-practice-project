package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class SaveBasicTranslationModel implements ISaveBasicTranslationModel {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              id;
  protected Map<String, IBasicTranslationModel> translations;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  @JsonDeserialize(contentAs = BasicTranslationModel.class)
  public Map<String, IBasicTranslationModel> getTranslations()
  {
    return translations;
  }
  
  @Override
  public void setTranslations(Map<String, IBasicTranslationModel> translations)
  {
    this.translations = translations;
  }
}
