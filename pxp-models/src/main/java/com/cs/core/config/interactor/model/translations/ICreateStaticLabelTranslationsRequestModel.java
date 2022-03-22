package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICreateStaticLabelTranslationsRequestModel extends IModel {
  
  public static final String STATIC_TRANSLATIONS = "staticTranslations";
  
  public Map<String, Map<String, Object>> getStaticTranslations();
  
  public void setStaticTranslations(Map<String, Map<String, Object>> staticTranslations);
}
