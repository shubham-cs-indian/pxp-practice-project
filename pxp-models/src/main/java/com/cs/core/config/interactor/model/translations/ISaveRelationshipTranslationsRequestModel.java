package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveRelationshipTranslationsRequestModel extends IModel {
  
  public static final String LANGAUGES   = "languages";
  public static final String DATA        = "data";
  public static final String ENTITY_TYPE = "entityType";
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
  
  public List<ISaveRelationshipTranslationModel> getData();
  
  public void setData(List<ISaveRelationshipTranslationModel> data);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
