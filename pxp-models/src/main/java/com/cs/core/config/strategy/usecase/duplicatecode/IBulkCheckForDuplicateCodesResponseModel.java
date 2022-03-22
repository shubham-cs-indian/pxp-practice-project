package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IBulkCheckForDuplicateCodesResponseModel extends IModel {
  
  public static final String ENTITY_CODE_CHECK = "entityCodeCheck";
  
  public Map<String, IBulkCheckForDuplicateCodesReturnModel> getEntityCodeCheck();
  
  public void setEntityCodeCheck(
      Map<String, IBulkCheckForDuplicateCodesReturnModel> entityCodeCheck);
}
