package com.cs.core.config.interactor.model.user;

import java.util.Map;

public class ValidateUserRequestModel implements IValidateUserRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Map<String, String>   validationMap;
 
  @Override
  public Map<String, String> getValidationMap()
  {
    return validationMap;
  }

  @Override
  public void setValidationMap(Map<String, String> validationMap)
  {
       this.validationMap = validationMap;
  }
}
