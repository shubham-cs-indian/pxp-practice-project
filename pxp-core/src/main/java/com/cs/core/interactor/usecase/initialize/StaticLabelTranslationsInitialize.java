package com.cs.core.interactor.usecase.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.initialize.IInitializeStaticLabelTranslationsService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractInteractor;


@Service
public class StaticLabelTranslationsInitialize extends AbstractInteractor<IVoidModel, IVoidModel>
    implements IStaticLabelTranslationsInitialize {
  
  @Autowired
  IInitializeStaticLabelTranslationsService initializeStaticLabelTranslationsService;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    initializeStaticLabelTranslationsService.execute();
    return null;
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
