package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAssetWithGlobalPermissionService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetWithGlobalPermission
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetAssetWithGlobalPermission {
  
  @Autowired
  IGetAssetWithGlobalPermissionService getAssetWithGlobalPermissionService;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getAssetWithGlobalPermissionService.execute(idModel);
  }
}
