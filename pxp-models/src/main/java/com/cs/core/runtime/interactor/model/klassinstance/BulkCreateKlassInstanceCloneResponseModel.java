package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkCreateKlassInstanceCloneResponseModel
    implements IBulkCreateKlassInstanceCloneResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected IExceptionModel              failure;
  protected List<IGetKlassInstanceModel> success;
  
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
  public Object getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetKlassInstanceForCustomTabModel.class)
  public void setSuccess(List<IGetKlassInstanceModel> success)
  {
    this.success = success;
  }
}
