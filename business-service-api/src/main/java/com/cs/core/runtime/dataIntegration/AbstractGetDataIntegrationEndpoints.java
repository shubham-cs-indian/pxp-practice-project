package com.cs.core.runtime.dataIntegration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointsForDashboardByUserIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.dataintegration.DataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetEndpointsInformationForDataIntegrationStrategy;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetDataIntegrationEndpoints<P extends IDataIntegrationRequestModel, R extends IDataIntegrationInfoReturnModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                    context;
  
  @Autowired
  protected IGetEndpointsForDashboardByUserIdStrategy          getEndpointsForDashboardByUserIdStrategy;
  
  @Autowired
  protected IGetEndpointsInformationForDataIntegrationStrategy getEndpointsInformationForDataIntegrationStrategy;
  
  protected R executeInternal(P model) throws Exception
  {
    model.setUserId(context.getUserId());
    IGetEndointsInfoModel endpointsInfoModel = getEndpointsForDashboardByUserIdStrategy
        .execute(model);
    endpointsInfoModel.setMode(model.getMode());

    IListModel<IDataIntegrationInfoModel> endpointInfoForDI = 
        getEndpointsInformationForDataIntegrationStrategy.execute(endpointsInfoModel);
    
    IDataIntegrationInfoReturnModel dataIntegrationInfoReturnModel = new DataIntegrationInfoReturnModel();
    dataIntegrationInfoReturnModel.setFunctionPermission(endpointsInfoModel.getFunctionPermisson());
    dataIntegrationInfoReturnModel.setDataIntegrationInfo(new ArrayList<>(endpointInfoForDI.getList()));
    
    return (R) dataIntegrationInfoReturnModel;  
  }
}
