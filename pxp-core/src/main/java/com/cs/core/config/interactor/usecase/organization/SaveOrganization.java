package com.cs.core.config.interactor.usecase.organization;

import com.cs.core.config.organization.ISaveOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.config.strategy.usecase.organization.ISaveOrganizationStrategy;

@Service
public class SaveOrganization
    extends AbstractSaveConfigInteractor<ISaveOrganizationModel, IGetOrganizationModel>
    implements ISaveOrganization {
  
  @Autowired
  protected ISaveOrganizationService saveOrganizationService;
  
  @Override
  public IGetOrganizationModel executeInternal(ISaveOrganizationModel model) throws Exception
  {
    return saveOrganizationService.execute(model);
  }
}
