package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.BulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.imprt.config.store.strategy.base.mapping.ICloneMappingsStrategy;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("cloneMappingsStrategy")
public class OrientDBCloneMappingsStrategy extends OrientDBBaseStrategy implements ICloneMappingsStrategy{

  
  public static final String useCase = "CloneMappings";

  @Override
  public IBulkSaveMappingsResponseModel execute(IListModel<IConfigCloneEntityInformationModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(useCase, requestMap, BulkSaveMappingsResponseModel.class);
  }
  
}
