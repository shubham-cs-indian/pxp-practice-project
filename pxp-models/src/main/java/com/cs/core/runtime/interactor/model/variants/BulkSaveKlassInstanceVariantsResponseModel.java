package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveKlassInstanceVariantsResponseModel
    implements IBulkSaveKlassInstanceVariantsResponseModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected IExceptionModel                      failure;
  protected IGetVariantInstancesInTableViewModel tableViewModel;
  
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
  
  @Override
  public IGetVariantInstancesInTableViewModel getSuccess()
  {
    return tableViewModel;
  }
  
  @JsonDeserialize(as = GetVariantInstancesInTableViewModel.class)
  @Override
  public void setSuccess(IGetVariantInstancesInTableViewModel tableViewModel)
  {
    this.tableViewModel = tableViewModel;
  }
}
