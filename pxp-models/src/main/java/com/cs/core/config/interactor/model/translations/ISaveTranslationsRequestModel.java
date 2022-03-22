package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveTranslationsRequestModel extends IModel {
  
  public static String       LANGAUGES   = "languages";
  public static final String ENTITY_TYPE = "entityType";
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
