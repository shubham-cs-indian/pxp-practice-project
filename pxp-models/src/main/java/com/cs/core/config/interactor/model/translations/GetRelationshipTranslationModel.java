package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetRelationshipTranslationModel implements IGetRelationshipTranslationModel {
  
  private static final long                            serialVersionUID = 1L;
  protected String                                     id;
  protected String                                     label;
  protected String                                     side1Label;
  protected String                                     side2Label;
  protected Map<String, IRelationshipTranslationModel> translations;
  protected String                                     code;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getSide1Label()
  {
    return side1Label;
  }
  
  @Override
  public void setSide1Label(String side1Label)
  {
    this.side1Label = side1Label;
  }
  
  @Override
  public String getSide2Label()
  {
    return side2Label;
  }
  
  @Override
  public void setSide2Label(String side2Label)
  {
    this.side2Label = side2Label;
  }
  
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
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
}
