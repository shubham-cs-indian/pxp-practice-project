package com.cs.config.interactor.usecase.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractSaveConfigInteractor<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigInteractor<P, R> implements ISaveConfigInteractor<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.UPDATED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    return super.execute(dataModel);
  }
}
