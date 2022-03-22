package com.cs.config.interactor.usecase.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractCreateConfigInteractor<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigInteractor<P, R> implements ICreateConfigInteractor<P, R> {
  
  public static final String IS_CLONE = "isClone";
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.CREATED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    return super.execute(dataModel);
  }
  
}
