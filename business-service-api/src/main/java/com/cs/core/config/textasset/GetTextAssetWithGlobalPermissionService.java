package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.textasset.IGetTextAssetWithGlobalPermissionStrategy;
import com.cs.core.config.textasset.IGetTextAssetWithGlobalPermissionService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTextAssetWithGlobalPermissionService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetTextAssetWithGlobalPermissionService {
  
  @Autowired
  IGetTextAssetWithGlobalPermissionStrategy getTextAssetWithGlobalPermissionStrategy;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTextAssetWithGlobalPermissionStrategy.execute(idModel);
  }
}
