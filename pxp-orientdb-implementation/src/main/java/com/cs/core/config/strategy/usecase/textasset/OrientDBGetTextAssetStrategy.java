package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.interactor.model.textasset.TextAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getTextAssetStrategy")
public class OrientDBGetTextAssetStrategy extends OrientDBBaseStrategy
    implements IGetTextAssetStrategy {
  
  public static final String useCase = "GetTextAsset";
  
  @Override
  public ITextAssetModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, TextAssetModel.class);
  }
}
