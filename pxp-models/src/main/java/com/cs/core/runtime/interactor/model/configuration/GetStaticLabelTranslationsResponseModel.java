package com.cs.core.runtime.interactor.model.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetStaticLabelTranslationsResponseModel
    implements IGetStaticLabelTranslationsResponseModel {
  
  private static final long     serialVersionUID        = 1L;
  
  protected Map<String, Object> staticLabelTranslations = new HashMap<>();
  
  @Override
  public Map<String, Object> getStaticLabelTranslations()
  {
    return staticLabelTranslations;
  }
  
  @JsonDeserialize(as = Map.class)
  @Override
  public void setStaticLabelTranslations(Map<String, Object> staticLabelTranslations)
  {
    this.staticLabelTranslations = staticLabelTranslations;
  }
}
