package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;

public interface IGetAllOffboardingEndpointsForUserService extends
    IConfigService<IGetOffboardingEndpointsByUserRequestModel, IListModel<IConfigEntityInformationModel>> {
  
}
