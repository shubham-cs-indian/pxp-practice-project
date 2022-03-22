package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllMappedEndpointsStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllMappedEndpointsStrategy") public class OrientDBGetAllMappedEndpointsStrategy
    extends OrientDBBaseStrategy implements IGetAllMappedEndpointsStrategy {

  public static final String useCase = "GetAllMappedEndpoints";
  
  @Override
  public IGetMappingForImportResponseModel execute(IGetMappedEndpointRequestModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetMappedEndpointRequestModel.CURRENT_USER_ID, model.getCurrentUserId());
    requestMap.put(IGetMappedEndpointRequestModel.FILE_HEADERS, model.getFileHeaders());
    requestMap.put(IGetMappedEndpointRequestModel.BOARDING_TYPE, model.getBoardingType());
    return execute(useCase, requestMap, GetMappingForImportResponseModel.class);
  }

}
