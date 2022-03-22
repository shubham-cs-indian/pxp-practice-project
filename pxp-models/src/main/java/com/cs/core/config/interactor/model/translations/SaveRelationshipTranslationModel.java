package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class SaveRelationshipTranslationModel implements ISaveRelationshipTranslationModel {
  
  private static final long                            serialVersionUID = 1L;
  protected String                                     id;
  protected Map<String, IRelationshipTranslationModel> translations;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Map<String, IRelationshipTranslationModel> getTranslations()
  {
    return translations;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipTranslationsModel.class)
  public void setTranslations(Map<String, IRelationshipTranslationModel> translations)
  {
    this.translations = translations;
  }
}
