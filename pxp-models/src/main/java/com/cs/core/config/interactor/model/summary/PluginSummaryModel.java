package com.cs.core.config.interactor.model.summary;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PluginSummaryModel extends ConfigResponseWithAuditLogModel
    implements IPluginSummaryModel {
  
  String                         type;
  IExceptionModel                failure;
  List<ISummaryInformationModel> success;
  List<ISummaryInformationModel> failedIds;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
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
  
  @Override
  public List<ISummaryInformationModel> getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setSuccess(List<ISummaryInformationModel> success)
  {
    this.success = success;
  }
  
  @Override
  public List<ISummaryInformationModel> getFailedIds()
  {
    return failedIds;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setFailedIds(List<ISummaryInformationModel> failedIds)
  {
    this.failedIds = failedIds;
  }
}
