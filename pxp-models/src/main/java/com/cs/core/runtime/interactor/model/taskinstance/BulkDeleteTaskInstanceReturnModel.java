package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteTaskInstanceReturnModel extends ConfigResponseWithAuditLogModel implements IBulkDeleteTaskInstanceReturnModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, Object> success          = new HashMap<String, Object>();
  protected IExceptionModel     failure;
  
  @Override
  public void setTaskCount(long taskCount)
  {
    this.success.put(TASK_COUNT, taskCount);
  }

  @Override
  public Map<String, Object> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<String> ids)
  {
    this.success.put(IDS, ids);
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
