package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSupplierWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSupplierWithGlobalPermissionService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetSupplierWithGlobalPermissionService {
  
  @Autowired
  IGetSupplierWithGlobalPermissionStrategy getSupplierWithGlobalPermissionStrategy;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierWithGlobalPermissionStrategy.execute(idModel);
  }
}
