package com.cs.core.config.interactor.usecase.organization;

import com.cs.core.config.organization.IGetOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.strategy.usecase.organization.IGetOrganizationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetOrganization
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetOrganizationModel>
    implements IGetOrganization {
  
  @Autowired
  protected IGetOrganizationService getOrganizationService;
  
  @Override
  public IGetOrganizationModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getOrganizationService.execute(idModel);
  }
}
