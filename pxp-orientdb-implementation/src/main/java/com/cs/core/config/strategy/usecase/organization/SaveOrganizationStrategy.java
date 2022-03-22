package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.GetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveOrganizationStrategy extends OrientDBBaseStrategy
    implements ISaveOrganizationStrategy {
  
  @Override
  public IGetOrganizationModel execute(ISaveOrganizationModel model) throws Exception
  {
    return execute(SAVE_ORGANIZATION, model, GetOrganizationModel.class);
  }
}
