  package com.cs.config.interactor.usecase.base;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;

public abstract class AbstractConfigImportExportInteractor<P extends IModel, R extends IModel>
    extends AbstractInteractor<P, R> implements IConfigInteractor<P, R> {
 
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.TALENDIMPORTEXPORT;
  }
}
