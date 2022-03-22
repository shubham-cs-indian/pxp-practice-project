package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.GetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateOrganizationStrategy extends OrientDBBaseStrategy
    implements ICreateOrganizationStrategy {
  
  @Override
  public IGetOrganizationModel execute(IOrganizationModel model) throws Exception
  {
    return execute(CREATE_ORGANIZATION, model, GetOrganizationModel.class);
  }
}
