package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.entity.klass.Asset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetsByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAssetsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetAssetsByIdsStrategy {
  
  @Override
  public IListModel<IKlass> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return super.execute(OrientDBBaseStrategy.GET_ASSETS_BY_IDS, requestMap,
        new TypeReference<ListModel<Asset>>()
        {
          
        });
  }
}
