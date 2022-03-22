package com.cs.core.runtime.interactor.model.offboarding;

import java.util.Map;

public class AdditionalPropertyDIModel implements IAdditionalPropertyDIModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, String> additionalProperty;
  
  @Override
  public Map<String, String> getAdditionalProperty()
  {
    return additionalProperty;
  }
  
  @Override
  public void setAdditionalProperty(Map<String, String> additionalProperty)
  {
    this.additionalProperty = additionalProperty;
  }
}
