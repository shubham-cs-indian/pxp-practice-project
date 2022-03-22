package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateTextAssetStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateTextAssetStrategy {
  
  public static final String useCase = "GetOrCreateTextAsset";
  
  @Override
  public ITextAssetModel execute(IListModel<ITextAssetModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    execute(useCase, requestMap);
    /*ITextAsset savedTextAssetKlass = ObjectMapperUtil.readValue(response, ITextAsset.class);
    return new TextAssetModel((TextAsset) savedTextAssetKlass);*/
    return null;
  }
}
