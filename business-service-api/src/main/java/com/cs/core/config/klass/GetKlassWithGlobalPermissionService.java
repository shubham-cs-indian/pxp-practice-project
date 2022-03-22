package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.klass.IGetKlassWithGlobalPermissionService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassWithGlobalPermissionService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetKlassWithGlobalPermissionService {
  
  @Autowired
  IGetKlassWithGlobalPermissionStrategy getKlassWithGLobalPermissionStrategy;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getKlassWithGLobalPermissionStrategy.execute(klassModel);
  }
}
