package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetOrCreateEndPointStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getOrCreateEndPointStrategy") public class OrientDBGetOrCreateEndPointStrategy
    extends OrientDBBaseStrategy implements IGetOrCreateEndPointStrategy {

  public static final String useCase = "GetOrCreateEndpoint";

  @Override public IModel execute(IListModel<IEndpoint> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model.getList());
    execute(useCase, requestMap);
    return null;
  }

}
