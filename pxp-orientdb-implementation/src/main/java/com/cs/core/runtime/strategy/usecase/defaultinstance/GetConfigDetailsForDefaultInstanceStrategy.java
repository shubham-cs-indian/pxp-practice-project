package com.cs.core.runtime.strategy.usecase.defaultinstance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

@Component
public class GetConfigDetailsForDefaultInstanceStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForDefaultInstanceStrategy {
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(GET_CONFIG_DETAILS_FOR_DEFAULT_INSTANCE, model, GetConfigDetailsForCustomTabModel.class);
  }
  
}
