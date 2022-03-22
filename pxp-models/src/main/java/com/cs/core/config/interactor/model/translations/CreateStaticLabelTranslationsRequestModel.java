package com.cs.core.config.interactor.model.translations;

import java.util.HashMap;
import java.util.Map;

public class CreateStaticLabelTranslationsRequestModel
    implements ICreateStaticLabelTranslationsRequestModel {
  
  private static final long                  serialVersionUID   = 1L;
  protected Map<String, Map<String, Object>> staticTranslations = new HashMap<>();
  
  public Map<String, Map<String, Object>> getStaticTranslations()
  {
    return staticTranslations;
  }
  
  public void setStaticTranslations(Map<String, Map<String, Object>> staticTranslations)
  {
    this.staticTranslations = staticTranslations;
  }
}
