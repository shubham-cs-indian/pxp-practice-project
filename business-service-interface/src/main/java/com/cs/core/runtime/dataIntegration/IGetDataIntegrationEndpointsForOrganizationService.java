package com.cs.core.runtime.dataIntegration;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;

public interface IGetDataIntegrationEndpointsForOrganizationService extends
    IRuntimeService<IDataIntegrationRequestModel, IDataIntegrationInfoReturnModel> {
}
