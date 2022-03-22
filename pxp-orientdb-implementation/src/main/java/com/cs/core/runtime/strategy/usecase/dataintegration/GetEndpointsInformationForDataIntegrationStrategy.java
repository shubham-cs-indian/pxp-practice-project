package com.cs.core.runtime.strategy.usecase.dataintegration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointBasicInfoModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dataintegration.DataIntegrationInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoModel;

@Component("getEndpointsInformationForDataIntegrationStrategy")
public class GetEndpointsInformationForDataIntegrationStrategy implements IGetEndpointsInformationForDataIntegrationStrategy {

  @Override
  public IListModel<IDataIntegrationInfoModel> execute(IGetEndointsInfoModel model) throws Exception
  {
    List<IDataIntegrationInfoModel> dataIntegrationInfoModels = new ArrayList<>();
    
    String mode = model.getMode();
    for (IEndpointBasicInfoModel endpoint: model.getEndpoints()) {
      IDataIntegrationInfoModel dataIntegrationInfoModel = new DataIntegrationInfoModel();
      dataIntegrationInfoModel.setMode(mode);
      dataIntegrationInfoModel.setCode(endpoint.getCode());
      dataIntegrationInfoModel.setId(endpoint.getId());
      dataIntegrationInfoModel.setIcon(endpoint.getIcon());
      dataIntegrationInfoModel.setIconKey(endpoint.getIconKey());
      dataIntegrationInfoModel.setLabel(endpoint.getLabel());
      dataIntegrationInfoModel.setType(endpoint.getType());
      dataIntegrationInfoModel.setPhysicalCatalogId(endpoint.getPhysicalCatalogId());
      
      //TODO: preview data on tile.
      dataIntegrationInfoModels.add(dataIntegrationInfoModel);
    }
    return new ListModel<IDataIntegrationInfoModel>(dataIntegrationInfoModels);
  }
}
