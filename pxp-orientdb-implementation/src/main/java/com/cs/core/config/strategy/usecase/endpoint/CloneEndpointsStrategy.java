package com.cs.core.config.strategy.usecase.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.BulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.ICloneEndpointsStrategy;

@Component("cloneEndpointsStrategy")
public class CloneEndpointsStrategy extends OrientDBBaseStrategy
    implements ICloneEndpointsStrategy {
  
  @Override
  public IBulkSaveEndpointsResponseModel execute(IListModel<ICloneEndpointModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(CLONE_ENDPOINTS, requestMap, BulkSaveEndpointsResponseModel.class);
  }
  
}
