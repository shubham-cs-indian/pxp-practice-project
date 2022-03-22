package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IAdditionalPropertyDIModel extends IModel {
  
  public static final String ADDITIONAL_PROPERTY = "additionalProperty";
  
  public Map<String, String> getAdditionalProperty();
  
  public void setAdditionalProperty(Map<String, String> map);
}
