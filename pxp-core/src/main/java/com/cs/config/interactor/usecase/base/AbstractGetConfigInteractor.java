package com.cs.config.interactor.usecase.base;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetConfigInteractor<P extends IModel, R extends IModel>
    extends AbstractConfigInteractor<P, R> implements IGetConfigInteractor<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }
}
