package com.cs.core.config.interactor.model.governancerule;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveKpiRuleSuccessModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveKpiRuleResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveKpiRuleResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected IBulkSaveKpiRuleSuccessModel success;
  protected IExceptionModel              failure;
  
  @Override
  public IBulkSaveKpiRuleSuccessModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkSaveKpiRuleSuccessModel.class)
  @Override
  public void setSuccess(IBulkSaveKpiRuleSuccessModel success)
  {
    this.success = success;
  }
  
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
}
