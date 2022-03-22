package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.GetMappedMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetMappedMappingRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetMappedMappingResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetAllMappedMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllMappedMappingStrategy") public class OrientDBGetAllMappedMappingStrategy
    extends OrientDBBaseStrategy implements IGetAllMappedMappingStrategy {

  public static final String useCase = "GetAllMappedEndpoints";
  @Override
  public IGetMappedMappingResponseModel execute(IGetMappedMappingRequestModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetMappedMappingRequestModel.CURRENT_USER_ID, model.getCurrentUserId());
    requestMap.put(IGetMappedMappingRequestModel.FILE_HEADERS, model.getFileHeaders());
    return execute(useCase, requestMap, GetMappedMappingResponseModel.class);
  }

}
