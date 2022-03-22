package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

import java.util.List;

public class SaveKlassInstanceModel implements ISaveKlassInstanceModel {
  
  protected IExceptionModel        failure;
  protected List<? extends IModel> success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public List<? extends IModel> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<? extends IModel> success)
  {
    this.success = success;
  }
}
