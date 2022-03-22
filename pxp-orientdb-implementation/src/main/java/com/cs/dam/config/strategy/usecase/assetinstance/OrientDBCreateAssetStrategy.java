package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.ICreateAssetStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateAssetStrategy extends OrientDBBaseStrategy
    implements ICreateAssetStrategy {
  
  public static final String useCase = "CreateAsset";
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IAssetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("asset", model.getEntity());
    return super.execute(useCase, requestMap, GetKlassEntityWithoutKPModel.class);
  }
}
