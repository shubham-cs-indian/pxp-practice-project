package com.cs.config.businessapi.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractSaveConfigService<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigService<P, R> implements ISaveConfigService<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.UPDATED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    R response = super.execute(dataModel);
    auditLog(response.getAuditLogInfo());
    
    return response;
  }
}
