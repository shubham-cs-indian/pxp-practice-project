  package com.cs.config.businessapi.base;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractConfigImportExportService<P extends IModel, R extends IModel>
    extends AbstractService<P, R> implements IConfigService<P, R> {
 
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.TALENDIMPORTEXPORT;
  }
}
