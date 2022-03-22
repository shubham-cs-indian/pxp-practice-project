package com.cs.core.config.interactor.usecase.organization;

import com.cs.core.config.organization.IGetAllOrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.organization.IGetAllOrganizationsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;

@Service
public class GetAllOrganizations
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllOrganizationResponseModel>
    implements IGetAllOrganizations {
  
  @Autowired
  protected IGetAllOrganizationsService getAllOrganizationsService;
  
  @Override
  public IGetAllOrganizationResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getAllOrganizationsService.execute(dataModel);
  }
}
