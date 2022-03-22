package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.pim.runtime.targetinstance.market.IRollbackMarketInstanceVersionService;

@Service
public class RollbackMarketInstanceVersion implements IRollbackMarketInstanceVersion {
  
  @Autowired
  IRollbackMarketInstanceVersionService rollbackMarketInstanceVersionService;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel) throws Exception
  {
    return rollbackMarketInstanceVersionService.execute(dataModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
}
