package com.cs.core.runtime.strategy.usecase.asset;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.AssetExportAPIResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetConfigDetailsForAssetExportAPIStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForAssetExportAPIStrategy {
  
  @Override public IAssetExportAPIResponseModel execute(IAssetExportAPIRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_FOR_ASSET_EXPORT_API, model, AssetExportAPIResponseModel.class);
  }

}
