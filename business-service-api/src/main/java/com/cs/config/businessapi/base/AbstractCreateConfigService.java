package com.cs.config.businessapi.base;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractCreateConfigService<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractConfigService<P, R> implements ICreateConfigService<P, R> {
  
  public static final String IS_CLONE = "isClone";
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.CREATED;
  }
  
  public R execute(P dataModel) throws Exception
  {
    if(dataModel instanceof IConfigModel) {
      IConfigModel model = (IConfigModel) dataModel;
      Validations.validateCode(model.getCode());
    }
    R response = super.execute(dataModel);
    auditLog(response.getAuditLogInfo());
    
    return response;
  }
  
}
