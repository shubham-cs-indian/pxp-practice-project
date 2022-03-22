package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.ISaveAssetStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveAssetStrategy extends OrientDBBaseStrategy implements ISaveAssetStrategy {
  
  public static final String useCase = "SaveAsset";
  
  @Override
  public IGetKlassEntityWithoutKPStrategyResponseModel execute(IKlassSaveModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("asset", model);
    return super.execute(useCase, requestMap, GetKlassEntityWithoutKPStrategyResponseModel.class);
  }
}
