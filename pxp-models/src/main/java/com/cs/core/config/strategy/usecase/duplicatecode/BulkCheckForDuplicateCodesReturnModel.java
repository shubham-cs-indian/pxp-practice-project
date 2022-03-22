package com.cs.core.config.strategy.usecase.duplicatecode;

import java.util.HashMap;
import java.util.Map;

public class BulkCheckForDuplicateCodesReturnModel
    implements IBulkCheckForDuplicateCodesReturnModel {
  
  private static final long                   serialVersionUID = 1L;
  protected Map<String, Map<String, Boolean>> codeCheck        = new HashMap<>();
  protected Map<String, Map<String, Boolean>> nameCheck        = new HashMap<>();

  
  @Override
  public Map<String, Map<String, Boolean>> getCodeCheck()
  {
    return codeCheck;
  }
  
  @Override
  public void setCodeCheck(Map<String, Map<String, Boolean>> codeCheck)
  {
    this.codeCheck = codeCheck;
  }

  @Override
  public Map<String, Map<String, Boolean>> getNameCheck()
  {
    return nameCheck;
  }

  @Override
  public void setNameCheck(Map<String, Map<String, Boolean>> nameCheck)
  {
    this.nameCheck = nameCheck;    
  }
}
