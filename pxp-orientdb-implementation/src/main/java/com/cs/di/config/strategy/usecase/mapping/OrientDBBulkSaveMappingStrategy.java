package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IBulkSaveMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("bulkSaveMappingStrategy") public class OrientDBBulkSaveMappingStrategy
    extends OrientDBBaseStrategy implements IBulkSaveMappingStrategy {

  public static final String useCase = "BulkSaveMapping";

  @Override public IBulkSaveMappingResponseModel execute(IBulkSaveMappingModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getModifiedMappings());
    return execute(useCase, requestMap, new TypeReference<ListModel<MappingModel>>()
    {
    });
  }

}
