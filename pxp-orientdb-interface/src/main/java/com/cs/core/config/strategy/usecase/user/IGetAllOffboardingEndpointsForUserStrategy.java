package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;

public interface IGetAllOffboardingEndpointsForUserStrategy
    extends IConfigStrategy<IGetOffboardingEndpointsByUserRequestModel, IListModel<IConfigEntityInformationModel>> {
  
}
