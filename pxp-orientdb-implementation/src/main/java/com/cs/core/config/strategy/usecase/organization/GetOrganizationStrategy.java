package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.GetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetOrganizationStrategy extends OrientDBBaseStrategy
    implements IGetOrganizationStrategy {
  
  @Override
  public IGetOrganizationModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ORGANIZATION, model, GetOrganizationModel.class);
  }
}
