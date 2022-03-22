package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.ICreateMarketInstanceVariantForLimitedObject;
import com.cs.core.runtime.variant.marketinstance.ICreateMarketInstanceVariantForLimitedObjectService;

@Service
public class CreateMarketInstanceVariantForLimitedObject implements ICreateMarketInstanceVariantForLimitedObject {
  
  @Autowired
  protected ICreateMarketInstanceVariantForLimitedObjectService createMarketInstanceVariantForLimitedObjectService;
  
  @Override
  public IGetVariantInstancesInTableViewModel execute(ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    return createMarketInstanceVariantForLimitedObjectService.execute(dataModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
}
