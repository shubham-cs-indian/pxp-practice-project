package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.CreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.ISaveOutBoundMappingStrategy;

@Component("saveOutBoundMappingStrategy")
public class SaveOutBoundMappingStrategy extends OrientDBBaseStrategy
    implements ISaveOutBoundMappingStrategy {
  
  @Override
  public ICreateOutBoundMappingResponseModel execute(ISaveOutBoundMappingModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(CommonConstants.MAPPING, model);
    return execute(OrientDBBaseStrategy.SAVE_OUTBOUND_MAPPING, requestMap,
        CreateOutBoundMappingResponseModel.class);
  }
}
