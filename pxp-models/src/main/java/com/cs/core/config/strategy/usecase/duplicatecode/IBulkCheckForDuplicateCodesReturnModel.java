package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IBulkCheckForDuplicateCodesReturnModel extends IModel {
  
  public static final String CODE_CHECK = "codeCheck";
  public static final String NAME_CHECK = "nameCheck";

  
  public Map<String, Map<String, Boolean>> getCodeCheck();
  
  public void setCodeCheck(Map<String, Map<String, Boolean>> codeCheck);
  
  public Map<String, Map<String, Boolean>> getNameCheck();
  
  public void setNameCheck(Map<String, Map<String, Boolean>> nameCheck);
}
