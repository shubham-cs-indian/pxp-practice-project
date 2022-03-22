package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IDeleteMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("deleteMappingStrategy")
public class OrientDBDeleteMappingStrategy extends OrientDBBaseStrategy
    implements IDeleteMappingStrategy {
  
  public static final String useCase = "DeleteMapping";

  @Override public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(useCase, requestMap, BulkDeleteReturnModel.class);
  }

}
