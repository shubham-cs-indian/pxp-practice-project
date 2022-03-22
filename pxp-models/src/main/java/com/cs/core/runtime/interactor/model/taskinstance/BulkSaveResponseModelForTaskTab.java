
package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveResponseModelForTaskTab implements IBulkSaveResponseModelForTaskTab {
  
  private static final long                   serialVersionUID = 1L;
  protected IListModel<IGetTaskInstanceModel> success;
  protected IExceptionModel                   failure;
  
  @Override
  public IExceptionModel getFailure()
  {
    if (failure == null) {
      failure = new ExceptionModel();
    }
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
    
  }
  
  @Override
  public IListModel<IGetTaskInstanceModel> getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = GetTaskInstanceModel.class)
  public void setSuccess(IListModel<IGetTaskInstanceModel> success)
  {
    this.success = success;
    
  }
  
}