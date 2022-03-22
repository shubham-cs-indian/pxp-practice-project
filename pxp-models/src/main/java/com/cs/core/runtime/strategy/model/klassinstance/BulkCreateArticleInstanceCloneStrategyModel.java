package com.cs.core.runtime.strategy.model.klassinstance;

import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.instance.SaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkCreateArticleInstanceCloneStrategyModel
    implements IBulkCreateArticleInstanceCloneStrategyModel {
  
  private static final long                          serialVersionUID = 1L;
  protected IExceptionModel                          failure;
  protected List<ISaveStrategyInstanceResponseModel> success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  @JsonDeserialize(as = ExceptionModel.class)
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Object getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveStrategyInstanceResponseModel.class)
  public void setSuccess(List<ISaveStrategyInstanceResponseModel> success)
  {
    this.success = success;
  }
}
