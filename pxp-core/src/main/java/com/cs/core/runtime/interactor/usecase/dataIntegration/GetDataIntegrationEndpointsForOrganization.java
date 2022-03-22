package com.cs.core.runtime.interactor.usecase.dataIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.dataIntegration.IGetDataIntegrationEndpointsForOrganizationService;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetDataIntegrationEndpointsForOrganization
    extends AbstractRuntimeInteractor<IDataIntegrationRequestModel, IDataIntegrationInfoReturnModel>
    implements IGetDataIntegrationEndpointsForOrganization {
  
  @Autowired
  protected IGetDataIntegrationEndpointsForOrganizationService getDataIntegrationEndpointsForOrganizationService;
  
  @Override
  public IDataIntegrationInfoReturnModel executeInternal(IDataIntegrationRequestModel model) throws Exception
  {
    return getDataIntegrationEndpointsForOrganizationService.execute(model);
  }
}
