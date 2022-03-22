package com.cs.config.businessapi.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractDeleteConfigService<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigService<P, R> implements IDeleteConfigService<P, R> {
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.DELETED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    R response = super.execute(dataModel);
    auditLog(response.getAuditLogInfo());
    
    return response;
  }
}
