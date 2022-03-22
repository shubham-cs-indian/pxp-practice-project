package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractRuntimeInteractor<P extends IModel, R extends IModel>
    extends AbstractInteractor<P, R> implements IRuntimeInteractor<P, R> {

  @Override
  public ServiceType getServiceType()
  {

    return null;
  }
}
