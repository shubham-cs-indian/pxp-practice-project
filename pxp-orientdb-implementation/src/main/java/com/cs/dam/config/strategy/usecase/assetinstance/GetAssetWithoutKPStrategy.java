package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithoutKPStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAssetWithoutKPStrategy")
public class GetAssetWithoutKPStrategy extends OrientDBBaseStrategy
    implements IGetAssetWithoutKPStrategy {
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_ASSET_WITHOUT_KP, requestMap,
        GetKlassEntityWithoutKPModel.class);
  }
}
