package com.cs.core.config.strategy.usecase.system;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.interactor.model.system.SystemModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetPaginatedSystemsStrategy extends OrientDBBaseStrategy
    implements IGetPaginatedSystemsStrategy {
  
  @Override
  public IListModel<ISystemModel> execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_PAGINATED_SYSTEMS, model, new TypeReference<ListModel<SystemModel>>()
    {
      
    });
  }
}
