package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.GetAllOrganizationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;
import org.springframework.stereotype.Component;

@Component
public class GetAllOrganizationsStrategy extends OrientDBBaseStrategy
    implements IGetAllOrganizationsStrategy {
  
  @Override
  public IGetAllOrganizationResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_ALL_ORGANIZATIONS, model, GetAllOrganizationResponseModel.class);
  }
}
