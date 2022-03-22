package com.cs.core.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class StaticLabelTranslationsInitializeService extends AbstractService<IVoidModel, IVoidModel>
    implements IStaticLabelTranslationsInitializeService {
  
  @Autowired
  protected IInitializeStaticLabelTranslationsService initializeStaticLabelTranslationsService;
  
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
