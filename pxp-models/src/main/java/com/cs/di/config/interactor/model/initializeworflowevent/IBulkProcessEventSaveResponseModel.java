package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkProcessEventSaveResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public IGetGridProcessEventsResponseModel getSuccess();
  
  public void setSuccess(IGetGridProcessEventsResponseModel success);
}
