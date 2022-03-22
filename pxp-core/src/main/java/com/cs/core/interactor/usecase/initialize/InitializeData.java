package com.cs.core.interactor.usecase.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.initialize.IInitializeDataService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;

@Service
public class InitializeData extends AbstractInteractor<IVoidModel, IVoidModel> implements IInitializeData {
  
  @Autowired
  protected IInitializeDataService initializeDataService;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    return initializeDataService.execute(dataModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
