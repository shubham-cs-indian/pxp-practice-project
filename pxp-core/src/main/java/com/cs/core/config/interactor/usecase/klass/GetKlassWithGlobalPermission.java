package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.klass.IGetKlassWithGlobalPermissionService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassWithGlobalPermission
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetKlassWithGlobalPermission {
  
  @Autowired
  IGetKlassWithGlobalPermissionService getKlassWithGLobalPermissionService;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getKlassWithGLobalPermissionService.execute(klassModel);
  }
}
