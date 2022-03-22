package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllEndpointsByTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

@Service
public class GetAllEndpointsByTypeService extends
    AbstractGetConfigService<IGetAllEndpointsByTypeRequestModel, IListModel<IIdLabelCodeModel>> implements IGetAllEndpointsByTypeService {
  
  @Autowired
  protected IGetAllEndpointsByTypeStrategy getAllEndpointsByTypeStrategy;
  
  @Override
  public IListModel<IIdLabelCodeModel> executeInternal(IGetAllEndpointsByTypeRequestModel dataModel) throws Exception
  {
    return getAllEndpointsByTypeStrategy.execute(dataModel);
  }
  
}
