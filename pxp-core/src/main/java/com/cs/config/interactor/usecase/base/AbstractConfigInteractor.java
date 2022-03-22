package com.cs.config.interactor.usecase.base;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class AbstractConfigInteractor<P extends IModel, R extends IModel>
    extends AbstractInteractor<P, R> implements IConfigInteractor<P, R> {
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.UNDEFINED;
  }
  
  @Override
  public String getEntity(P model)
  {
    return "ConfigData";
  }

}
