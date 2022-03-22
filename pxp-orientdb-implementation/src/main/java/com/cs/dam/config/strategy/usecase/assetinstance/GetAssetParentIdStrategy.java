package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetParentIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetAssetParentIdStrategy extends OrientDBBaseStrategy
    implements IGetAssetParentIdStrategy {
  
  @Override
  public IIdParameterModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ASSET_PARENT_ID, model, IdParameterModel.class);
  }
}
