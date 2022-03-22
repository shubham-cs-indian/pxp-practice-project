package com.cs.pim.runtime.interactor.version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.pim.runtime.articleinstance.IRollbackArticleInstanceVersionService;

@Service("rollbackArticleInstanceVersion")
public class RollbackArticleInstanceVersion  implements IRollbackArticleInstanceVersion {
  
  @Autowired
  protected IRollbackArticleInstanceVersionService rollbackArticleInstanceVersionService;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return rollbackArticleInstanceVersionService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }

}
