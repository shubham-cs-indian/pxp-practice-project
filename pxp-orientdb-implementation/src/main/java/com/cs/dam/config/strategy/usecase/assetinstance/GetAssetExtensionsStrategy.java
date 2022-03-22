package com.cs.dam.config.strategy.usecase.assetinstance;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.GetAssetExtensionsModel;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetExtensionsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;


@Component
public class GetAssetExtensionsStrategy extends OrientDBBaseStrategy implements IGetAssetExtensionsStrategy{ 
  
  @Override
  public IGetAssetExtensionsModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_ALL_ASSET_EXTENSIONS, model, GetAssetExtensionsModel.class);    
  }
  
}
