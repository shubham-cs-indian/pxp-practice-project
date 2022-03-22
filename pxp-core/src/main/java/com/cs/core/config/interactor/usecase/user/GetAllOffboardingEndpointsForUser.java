package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.user.IGetAllOffboardingEndpointsForUserService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;

@Service
public class GetAllOffboardingEndpointsForUser
    extends AbstractGetConfigInteractor<IGetOffboardingEndpointsByUserRequestModel, IListModel<IConfigEntityInformationModel>>
    implements IGetAllOffboardingEndpointsForUser {
  
  @Autowired
  protected IGetAllOffboardingEndpointsForUserService getAllOffboardingEndpointsForUserService;
  
  @Override
  public IListModel<IConfigEntityInformationModel> executeInternal(IGetOffboardingEndpointsByUserRequestModel dataModel) throws Exception
  {
    return getAllOffboardingEndpointsForUserService.execute(dataModel);
  }
}
