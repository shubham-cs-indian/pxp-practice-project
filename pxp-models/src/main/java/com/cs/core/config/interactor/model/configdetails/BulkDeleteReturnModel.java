package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteReturnModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeleteReturnModel {
  
  protected List<String>    success;
  protected IExceptionModel failure;
  
  @Override
  public List<String> getSuccess()
  {
    if (success == null) {
      success = new ArrayList<String>();
    }
    return success;
  }
  
  @Override
  public void setSuccess(List<String> ids)
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
