package com.cs.core.config.interactor.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.strategy.usecase.migration.IMigrateDefaultTranslationsStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class MagrateDefaultTranslations implements IMigrateDefaultTranslations {
  
  @Autowired
  IMigrateDefaultTranslationsStrategy migrateDefaultTranslationsStrategy;
  
  @Override
  public IVoidModel execute(IIdParameterModel dataModel) throws Exception
  {
    migrateDefaultTranslationsStrategy.execute(dataModel);
    
    return null;
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
