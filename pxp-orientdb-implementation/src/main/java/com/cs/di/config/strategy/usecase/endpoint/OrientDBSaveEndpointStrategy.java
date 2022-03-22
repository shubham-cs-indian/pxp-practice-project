package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.BulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.ISaveEndpointStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("saveEndpointStrategy")
public class OrientDBSaveEndpointStrategy extends OrientDBBaseStrategy implements ISaveEndpointStrategy {
  
  @Override
  public IBulkSaveEndpointsResponseModel execute(IListModel<ISaveEndpointModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(SAVE_ENDPOINT, requestMap, BulkSaveEndpointsResponseModel.class);
  }
  
}
