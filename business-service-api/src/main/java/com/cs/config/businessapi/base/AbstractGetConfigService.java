package com.cs.config.businessapi.base;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetConfigService<P extends IModel, R extends IModel>
    extends AbstractConfigService<P, R> implements IGetConfigService<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }
}
