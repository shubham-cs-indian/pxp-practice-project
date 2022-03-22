package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.CreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.ISaveMappingStrategy;

@Component("saveMappingStrategy")
public class OrientDBSaveMappingStrategy extends OrientDBBaseStrategy
    implements ISaveMappingStrategy {
  
  public static final String useCase = "SaveMapping";

  @Override public ICreateOrSaveMappingModel execute(ISaveMappingModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("mapping", model);
    return execute(useCase, requestMap, CreateOrSaveMappingModel.class);
  }

}
