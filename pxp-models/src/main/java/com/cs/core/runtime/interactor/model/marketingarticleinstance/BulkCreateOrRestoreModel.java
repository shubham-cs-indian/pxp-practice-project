package com.cs.core.runtime.interactor.model.marketingarticleinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkCreateOrRestoreModel implements IBulkCreateOrRestoreModel {
  
  private static final long        serialVersionUID = 1L;
  
  protected IIdsListParameterModel success;
  protected IExceptionModel        failure;
  
  @Override
  public IIdsListParameterModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = IdsListParameterModel.class)
  @Override
  public void setSuccess(IIdsListParameterModel success)
  {
    this.success = success;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
}
