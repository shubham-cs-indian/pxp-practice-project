package com.cs.core.config.interactor.model.translations;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface ISaveTranslationsResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(List<IGetPropertyTranslationsModel> success);
}
