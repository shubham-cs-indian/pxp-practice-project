package com.cs.core.config.interactor.usecase.organization;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.organization.ICreateOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateOrganization
    extends AbstractCreateConfigInteractor<IOrganizationModel, IGetOrganizationModel>
    implements ICreateOrganization {

  @Autowired
  protected ICreateOrganizationService createOrganizationService;


  @Override
  public IGetOrganizationModel executeInternal(IOrganizationModel model) throws Exception
  {
    return createOrganizationService.execute(model);
  }
}
