package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SaveRelationshipTranslationsRequestModel extends AbstractSaveTranslationsRequestModel
    implements ISaveRelationshipTranslationsRequestModel {
  
  private static final long                         serialVersionUID = 1L;
  protected List<ISaveRelationshipTranslationModel> data;
  protected String                                  entityType;
  
  @Override
  public List<ISaveRelationshipTranslationModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveRelationshipTranslationModel.class)
  public void setData(List<ISaveRelationshipTranslationModel> data)
  {
    this.data = data;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
