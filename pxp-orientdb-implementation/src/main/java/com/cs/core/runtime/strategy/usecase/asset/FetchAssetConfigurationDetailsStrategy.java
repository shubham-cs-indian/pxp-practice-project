package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.AssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component("fetchAssetConfigurationDetails")
public class FetchAssetConfigurationDetailsStrategy extends OrientDBBaseStrategy
    implements IFetchAssetConfigurationDetails {
  
  @Override
  public IAssetConfigurationDetailsResponseModel execute(IIdParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.FETCH_ASSET_CONFIGURATION_DETAILS, model,
        AssetConfigurationDetailsResponseModel.class);
  }
}
