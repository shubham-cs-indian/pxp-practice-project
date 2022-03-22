package com.cs.core.runtime.interactor.model.marketingarticleinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IBulkCreateOrRestoreModel extends IBulkResponseModel {
  
  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  
  public IIdsListParameterModel getSuccess();
  
  public void setSuccess(IIdsListParameterModel success);
}
