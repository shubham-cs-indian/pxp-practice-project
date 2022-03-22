package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTargetWithGlobalPermissionService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetTargetWithGlobalPermissionService {
  
  @Autowired
  IGetTargetWithGlobalPermissionStrategy getTargetWithGlobalPermissionStrategy;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetWithGlobalPermissionStrategy.execute(idModel);
  }
}
