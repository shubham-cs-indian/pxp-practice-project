package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.articleinstance.ICreateArticleInstanceVariantForLimitedObjectService;

@Service
public class CreateArticleInstanceVariantForLimitedObject implements ICreateArticleInstanceVariantForLimitedObject {
  
  @Autowired
  protected ICreateArticleInstanceVariantForLimitedObjectService createArticleInstanceVariantForLimitedObjectService;
  
  public IGetVariantInstancesInTableViewModel execute(ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    return createArticleInstanceVariantForLimitedObjectService.execute(dataModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
}
