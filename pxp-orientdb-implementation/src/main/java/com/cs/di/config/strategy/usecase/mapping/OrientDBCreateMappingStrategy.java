package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.CreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.ICreateMappingStrategy;

@Component("createMappingStrategy")
public class OrientDBCreateMappingStrategy extends OrientDBBaseStrategy
    implements ICreateMappingStrategy {
  
  public static final String useCase = "CreateMapping";

  @Override public ICreateOrSaveMappingModel execute(IMappingModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(CommonConstants.MAPPING, model);
    return execute(useCase, requestMap, CreateOrSaveMappingModel.class);
  }

}
