package com.cs.config.interactor.usecase.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractDeleteConfigInteractor<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigInteractor<P, R> implements IDeleteConfigInteractor<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.DELETED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    return super.execute(dataModel);
  }
}
