package com.cs.core.config.strategy.usecase.migration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@Component
public class MigrateIconsForIconLibraryStrategy extends OrientDBBaseStrategy
    implements IMigrateIconsForIconLibraryStrategy {

  @Override
  public IVoidModel execute(IGetAssetDetailsRequestModel model) throws Exception {
    Map<String, Object> assetRequestModel = new HashMap<>();
    assetRequestModel.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, model.getAssetServerDetails());
    
    return execute("MigrationToIconLibraryFromEntityIconProperty", assetRequestModel, VoidModel.class);
  }
}
