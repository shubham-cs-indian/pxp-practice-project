package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ISaveRelationshipTranslationModel extends IModel {
  
  public static final String ID           = "id";
  public static final String TRANSLATIONS = "translations";
  
  public String getId();
  
  public void setId(String id);
  
  public Map<String, IRelationshipTranslationModel> getTranslations();
  
  public void setTranslations(Map<String, IRelationshipTranslationModel> translations);
}
