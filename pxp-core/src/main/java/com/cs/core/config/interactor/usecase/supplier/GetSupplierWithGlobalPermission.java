package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.supplier.IGetSupplierWithGlobalPermissionService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSupplierWithGlobalPermission
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetSupplierWithGlobalPermission {
  
  @Autowired
  IGetSupplierWithGlobalPermissionService getSupplierWithGlobalPermissionService;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierWithGlobalPermissionService.execute(idModel);
  }
}
