package com.cs.core.config.interactor.model.goldenrecord;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveGoldenRecordRuleResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveGoldenRecordRuleResponseModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<IIdLabelCodeModel> success;
  protected IExceptionModel         failure;
  
  @Override
  public List<IIdLabelCodeModel> getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setSuccess(List<IIdLabelCodeModel> success)
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
