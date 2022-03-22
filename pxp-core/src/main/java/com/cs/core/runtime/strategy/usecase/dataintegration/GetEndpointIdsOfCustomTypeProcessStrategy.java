package com.cs.core.runtime.strategy.usecase.dataintegration;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.GetEndpointsAndOrganisationIdResponseModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdResponseModel;

@Component
public class GetEndpointIdsOfCustomTypeProcessStrategy extends OrientDBBaseStrategy
    implements IGetEndpointIdsOfCustomTypeProcessStrategy {
  
  public static final String useCase = "GetEndpointIdsOfCustomTypeProcess";
  
  @Override
  public IGetEndpointsAndOrganisationIdResponseModel execute(IGetEndpointsAndOrganisationIdRequestModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetEndpointsAndOrganisationIdRequestModel.PROCESS_IDS, model.getProcessIds());
    requestMap.put(IGetEndpointsAndOrganisationIdRequestModel.USER_ID, model.getUserId());
    
    return execute(useCase, requestMap, GetEndpointsAndOrganisationIdResponseModel.class);
  }
}
