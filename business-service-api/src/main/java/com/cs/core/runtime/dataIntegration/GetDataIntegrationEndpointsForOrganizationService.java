package com.cs.core.runtime.dataIntegration;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;

@Service
public class GetDataIntegrationEndpointsForOrganizationService extends
    AbstractGetDataIntegrationEndpoints<IDataIntegrationRequestModel, IDataIntegrationInfoReturnModel>
    implements IGetDataIntegrationEndpointsForOrganizationService {
  
  @Override
  public IDataIntegrationInfoReturnModel execute(IDataIntegrationRequestModel model)
      throws Exception
  {
    return super.executeInternal(model);
  }
}
