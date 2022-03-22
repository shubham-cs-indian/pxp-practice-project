package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.EndpointModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllowedEndpointsForRoleStrategy extends OrientDBBaseStrategy
    implements IGetAllowedEndpointsForRoleStrategy {
  
  @Override
  public IListModel<IEndpointModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ALLOWED_ENDPOINTS_FOR_ROLE, model,
        new TypeReference<ListModel<EndpointModel>>()
        {
          
        });
  }
}
