package com.cs.core.config.organization;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.strategy.usecase.organization.IGetOrganizationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrganizationService extends AbstractGetConfigService<IIdParameterModel, IGetOrganizationModel>
    implements IGetOrganizationService {
  
  @Autowired
  protected IGetOrganizationStrategy getOrganizationStrategy;
  
  @Override
  public IGetOrganizationModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getOrganizationStrategy.execute(idModel);
  }
}
