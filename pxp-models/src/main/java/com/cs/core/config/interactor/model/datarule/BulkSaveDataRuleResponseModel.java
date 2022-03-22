package com.cs.core.config.interactor.model.datarule;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveDataRuleResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveDataRuleResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected List<IBulkSaveDataRuleModel> success;
  protected IExceptionModel              failure;
  
  @Override
  public List<IBulkSaveDataRuleModel> getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(contentAs = BulkSaveDataRuleModel.class)
  @Override
  public void setSuccess(List<IBulkSaveDataRuleModel> success)
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
