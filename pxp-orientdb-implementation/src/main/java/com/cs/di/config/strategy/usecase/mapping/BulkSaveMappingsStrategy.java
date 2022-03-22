package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.BulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IBulkSaveMappingsStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

@Component
public class BulkSaveMappingsStrategy extends OrientDBBaseStrategy
    implements IBulkSaveMappingsStrategy {
  
  public static final String useCase = "BulkSaveMappings";

  @Override
  public IBulkSaveMappingsResponseModel execute(IListModel<IConfigEntityInformationModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(useCase, requestMap, BulkSaveMappingsResponseModel.class);
  }
}
