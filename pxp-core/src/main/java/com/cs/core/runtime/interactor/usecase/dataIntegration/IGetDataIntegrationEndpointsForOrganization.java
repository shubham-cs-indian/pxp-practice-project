package com.cs.core.runtime.interactor.usecase.dataIntegration;

import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetDataIntegrationEndpointsForOrganization extends
    IRuntimeInteractor<IDataIntegrationRequestModel, IDataIntegrationInfoReturnModel> {
}
