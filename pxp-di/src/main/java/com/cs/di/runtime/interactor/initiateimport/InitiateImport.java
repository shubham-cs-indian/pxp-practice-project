package com.cs.di.runtime.interactor.initiateimport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;
import com.cs.di.runtime.model.initiateimport.IImportDataModel;

@Service
public class InitiateImport extends AbstractInteractor<IImportDataModel, IVoidModel> implements IInitiateImport {
  
  @Autowired
  protected IInitiateImportService initiateImportService;
  
  @Override
  protected IVoidModel executeInternal(IImportDataModel model) throws Exception
  {
    return initiateImportService.execute(model);

    
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.IMPORT;
  }
}