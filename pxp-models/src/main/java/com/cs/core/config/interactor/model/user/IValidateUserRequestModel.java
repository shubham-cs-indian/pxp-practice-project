package com.cs.core.config.interactor.model.user;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IValidateUserRequestModel extends IModel {
  
  public static final String VALIDATION_MAP = "validationMap";
  
  public Map<String, String> getValidationMap();  
  public void setValidationMap(Map<String, String> validationMap);
}
