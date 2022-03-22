package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllEndpointsByTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component("getAllEndpointsByTypeStrategy") public class OrientDBGetAllEndpointsByTypeStrategy
    extends OrientDBBaseStrategy implements IGetAllEndpointsByTypeStrategy {

  public static final String useCase = "GetAllEndpointsByType";
  
  @Override
  public IListModel<IIdLabelCodeModel> execute(IGetAllEndpointsByTypeRequestModel model)
      throws Exception
  {
    return execute(useCase, model, new TypeReference<ListModel<IdLabelCodeModel>>()
    {
    });
  }
}
