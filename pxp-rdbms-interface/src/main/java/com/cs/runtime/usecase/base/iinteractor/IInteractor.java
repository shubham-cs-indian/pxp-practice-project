package com.cs.runtime.usecase.base.iinteractor;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IInteractor<P extends IModel, R extends IModel> {

  public R execute(P dataModel) throws Exception;

	public ServiceType getServiceType();
}
