package com.cs.core.businessapi.base;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractRuntimeService<P extends IModel, R extends IModel>
    extends AbstractService<P, R> implements IRuntimeService<P, R> {

  @Override
  public ServiceType getServiceType()
  {

    return null;
  }
}
