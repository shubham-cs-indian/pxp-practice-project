package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.config.interactor.model.role.SystemsVsEndpointsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetSelectedSystemInOrganizationStrategy extends OrientDBBaseStrategy
    implements IGetSelectedSystemInOrganizationStrategy {
  
  @Override
  public IListModel<ISystemsVsEndpointsModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_SELECTED_SYSTEMS_IN_ORGANIZATION, model,
        new TypeReference<ListModel<SystemsVsEndpointsModel>>()
        {
          
        });
  }
}
