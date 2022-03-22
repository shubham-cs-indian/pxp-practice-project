package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllowedTypesStrategy extends OrientDBBaseStrategy
    implements IGetAllowedTypesStrategy {
  
  public static final String useCase = "GetAllowedTypes";
  
  @Override
  public IListModel<String> execute(IGetAllowedTypesModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("map", model);
    return execute(useCase, requestMap, new TypeReference<ListModel<String>>()
    {
      
    });
  }
}
