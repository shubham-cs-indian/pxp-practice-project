package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.businessapi.tag.IGetAllAssetExtensionsService;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetExtensionsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetAllAssetExtensionsService
    extends AbstractGetConfigService<IIdsListParameterModel, IGetAssetExtensionsModel>
    implements IGetAllAssetExtensionsService {
  
  @Autowired
  protected IGetAssetExtensionsStrategy getAssetExtensionsStrategy;
  
  @Override
  protected IGetAssetExtensionsModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    IGetAssetExtensionsModel response = getAssetExtensionsStrategy.execute(model);
    return response;
  }
  
}
