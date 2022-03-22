package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.EndpointModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IBulkGetMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("bulkGetMappingStrategy") public class OrientDBBulkGetMappingStrategy
    extends OrientDBBaseStrategy implements IBulkGetMappingStrategy {

  public static final String useCase = "BulkGetEndpoint";

  @Override public IListModel<IMappingModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, new HashMap<>(), new TypeReference<ListModel<EndpointModel>>()
    {
    });
  }

}
