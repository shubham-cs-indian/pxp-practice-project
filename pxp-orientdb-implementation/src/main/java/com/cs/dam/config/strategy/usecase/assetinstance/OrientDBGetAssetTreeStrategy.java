package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassTreeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAssetTreeStrategy")
public class OrientDBGetAssetTreeStrategy extends OrientDBBaseStrategy
    implements IGetKlassTreeStrategy {
  
  @Override
  public IConfigEntityTreeInformationModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_ASSET_TREE, requestMap,
        ConfigEntityTreeInformationModel.class);
  }
}
