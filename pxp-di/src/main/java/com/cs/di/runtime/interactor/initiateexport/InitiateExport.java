package com.cs.di.runtime.interactor.initiateexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;
import com.cs.di.runtime.model.initiateexport.IExportDataModel;

@Service
public class InitiateExport extends AbstractInteractor<IExportDataModel, IModel> implements IInitiateExport {
  
  @Autowired
  protected IInitiateExportService initiateExportService;
  
  @Override
  protected IModel executeInternal(IExportDataModel exportDataModel) throws Exception
  {
    return initiateExportService.execute(exportDataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.EXPORT;
  }
}
