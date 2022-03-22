package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.textassetinstance.ICreateTextAssetInstanceVariantForLimitedObjectService;

@Service
public class CreateTextAssetInstanceVariantForLimitedObject implements ICreateTextAssetInstanceVariantForLimitedObject {

  @Autowired 
  protected ICreateTextAssetInstanceVariantForLimitedObjectService createTextAssetInstanceVariantForLimitedObjectService;
  
  @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    return createTextAssetInstanceVariantForLimitedObjectService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
