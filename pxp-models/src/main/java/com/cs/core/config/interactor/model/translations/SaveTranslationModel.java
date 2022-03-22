package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class SaveTranslationModel implements ISaveStandardTranslationModel {
  
  private static final long                        serialVersionUID = 1L;
  protected String                                 id;
  protected Map<String, IStandardTranslationModel> translations;
  
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
  @JsonDeserialize(contentAs = StandardTranslationModel.class)
  public Map<String, IStandardTranslationModel> getTranslations()
  {
    return translations;
  }
  
  @Override
  public void setTranslations(Map<String, IStandardTranslationModel> translations)
  {
    this.translations = translations;
  }
}
