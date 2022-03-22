package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithVersionIdModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListWithVersionIdModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteVariantsReturnModel implements IBulkDeleteVariantsReturnModel {
  
  private static final long            serialVersionUID = 1L;
  
  protected IIdsListWithVersionIdModel success;
  protected IExceptionModel            failure;
  
  @Override
  public IIdsListWithVersionIdModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = IdsListWithVersionIdModel.class)
  @Override
  public void setSuccess(IIdsListWithVersionIdModel ids)
  {
    
    this.success = ids;
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
