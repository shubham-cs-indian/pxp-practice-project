package com.cs.core.config.strategy.usecase.module;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component("getAllowedModuleEntitiesStrategy")
public class GetAllowedModuleEntitiesStrategy extends OrientDBBaseStrategy
    implements IGetAllowedModuleEntitiesStrategy {
  
  @Override
  public IListModel<String> execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ALLOWED_MODULE_ENTITIES, model, new TypeReference<ListModel<String>>()
    {
      
    });
  }
}
