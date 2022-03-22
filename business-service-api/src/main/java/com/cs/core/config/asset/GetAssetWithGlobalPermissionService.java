package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetWithGlobalPermissionService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetAssetWithGlobalPermissionService {
  
  @Autowired
  IGetAssetWithGlobalPermissionStrategy getAssetWithGlobalPermissionStrategy;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getAssetWithGlobalPermissionStrategy.execute(idModel);
  }
}
