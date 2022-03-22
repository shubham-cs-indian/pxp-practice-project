package com.cs.config.businessapi.base;

import com.cs.constants.CommonConstants;
import com.cs.core.exception.InvalidCodeException;
import com.cs.core.exception.LabelMustNotBeEmptyException;

public class Validations {

  public static Boolean isCodeValid(String code)
  {
    if (code.isEmpty() || code.isBlank()) {
      return false;
    }
    return code.matches("^[a-zA-Z0-9_-]+$");
  }

  protected static Boolean isEmpty(String field)
  {
    return field == null || field.isEmpty() || field.isBlank();
  }
  
  public static void validateCode(String code) throws InvalidCodeException
  {
    if(!isEmpty(code)) {
      if(!isCodeValid(code)) {
        throw new InvalidCodeException();
      }
    }
  }
  
  public static void validateLabel(String label) throws LabelMustNotBeEmptyException
  {
    if (isEmpty(label)) {
      throw new LabelMustNotBeEmptyException(CommonConstants.LABEL_PROPERTY + " can't be empty");
    }
  }
  
  public void validate(String code, String label) throws Exception
  {
    validateCode(code);
    validateLabel(label);
  }
  
}
