package com.cs.di.config.model.authorization;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

import java.util.List;

public interface IBulkSavePartnerAuthorizationModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(List<IIdLabelCodeModel> success);
}
