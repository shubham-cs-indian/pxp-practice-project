package com.cs.pim.runtime.interactor.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.pim.runtime.textassetinstance.IRollbackTextAssetInstanceVersionService;

@Service
public class RollbackTextAssetInstanceVersion implements IRollbackTextAssetInstanceVersion {
  
  @Autowired
  IRollbackTextAssetInstanceVersionService rollbackTextAssetInstanceVersionService;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel) throws Exception
  {
    return rollbackTextAssetInstanceVersionService.execute(dataModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
}
