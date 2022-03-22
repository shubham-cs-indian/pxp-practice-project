package com.cs.core.runtime.interactor.model.instance;

import java.util.Map;

public interface IChangeKlassTypeWithAdditionalInformationModel extends IChangeKlassTypeModel {
  
  public static String ADDITIONAL_PROPERTY = "additionalProperty";
  
  public Map<String, Object> getAdditionalProperty();
  
  public void setAdditionalProperty(Map<String, Object> additionalProperties);
}
