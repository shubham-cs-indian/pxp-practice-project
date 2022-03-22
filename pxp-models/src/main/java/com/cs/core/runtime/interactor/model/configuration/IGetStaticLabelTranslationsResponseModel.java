package com.cs.core.runtime.interactor.model.configuration;

import java.util.Map;

public interface IGetStaticLabelTranslationsResponseModel extends IModel {
  
  public static String STATIC_LABEL_TRANSLATIONS = "staticLabelTranslations";
  
  public Map<String, Object> getStaticLabelTranslations();
  
  public void setStaticLabelTranslations(Map<String, Object> staticLabelTranslations);
}
