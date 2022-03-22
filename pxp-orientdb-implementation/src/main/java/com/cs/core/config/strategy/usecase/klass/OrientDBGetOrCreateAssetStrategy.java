package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBGetOrCreateAssetStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateAssetStrategy {
  
  public static final String useCase = "GetOrCreateAsset";
  
  @Override
  public IAssetModel execute(IListModel<IAssetModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    /*IAsset savedAssetKlass = ObjectMapperUtil.readValue(response, IAsset.class);
    
    return new AssetModel((Asset) savedAssetKlass);*/
    execute(useCase, requestMap);
    return null;
  }
}
