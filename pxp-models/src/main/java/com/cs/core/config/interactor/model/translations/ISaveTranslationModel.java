package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ISaveTranslationModel extends IModel {
  
  public static final String ID           = "id";
  public static final String TRANSLATIONS = "translations";
  
  public String getId();
  
  public void setId(String id);
  
  public Map<String, ITranslationModel> getTranslations();
  
  public void setTranslations(Map<String, ITranslationModel> translations);
}
