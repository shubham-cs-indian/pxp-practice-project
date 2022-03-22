package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.user.IGetAllOffboardingEndpointsForUserStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;

@Service
public class GetAllOffboardingEndpointsForUserService extends
    AbstractGetConfigService<IGetOffboardingEndpointsByUserRequestModel, IListModel<IConfigEntityInformationModel>>
    implements IGetAllOffboardingEndpointsForUserService {
  
  @Autowired
  protected IGetAllOffboardingEndpointsForUserStrategy getAllOffboardingEndpointsForUserStrategy;
  
  @Override
  public IListModel<IConfigEntityInformationModel> executeInternal(IGetOffboardingEndpointsByUserRequestModel dataModel)
      throws Exception
  {
    return getAllOffboardingEndpointsForUserStrategy.execute(dataModel);
  }
}
