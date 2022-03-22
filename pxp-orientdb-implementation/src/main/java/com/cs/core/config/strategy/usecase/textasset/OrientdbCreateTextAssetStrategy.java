package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientdbCreateTextAssetStrategy extends OrientDBBaseStrategy
    implements ICreateTextAssetStrategy {
  
  public static final String useCase = "CreateTextAsset";
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(ITextAssetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("textasset", model.getEntity());
    return execute(useCase, requestMap, GetKlassEntityWithoutKPModel.class);
  }
}
