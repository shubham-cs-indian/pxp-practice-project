package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.assetinstance.ICreateAssetInstanceVariantForLimitedObjectService;

@Service
public class CreateAssetInstanceVariantForLimitedObject implements ICreateAssetInstanceVariantForLimitedObject {
  
  @Autowired
  protected ICreateAssetInstanceVariantForLimitedObjectService createAssetInstanceVariantForLimitedObjectService;
  
 @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    return createAssetInstanceVariantForLimitedObjectService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
