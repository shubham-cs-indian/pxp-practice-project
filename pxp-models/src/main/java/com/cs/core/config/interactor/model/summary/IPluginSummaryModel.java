package com.cs.core.config.interactor.model.summary;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IPluginSummaryModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static final String FAILED_IDS = "failedIds";
  
  public void setSuccess(List<ISummaryInformationModel> success);
  
  public List<ISummaryInformationModel> getFailedIds();
  
  public void setFailedIds(List<ISummaryInformationModel> failedIds);
  
  public String getType();
  
  public void setType(String type);
}
