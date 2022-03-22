package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;

public interface IGetAllOffboardingEndpointsForUser extends
    IConfigInteractor<IGetOffboardingEndpointsByUserRequestModel, IListModel<IConfigEntityInformationModel>> {
  
}
