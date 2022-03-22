package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllowedEndpointsForRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllowedEndpointsForRoleService extends AbstractGetConfigService<IIdParameterModel, IListModel<IEndpointModel>>
    implements IGetAllowedEndpointsForRoleService {
  
  @Autowired
  protected IGetAllowedEndpointsForRoleStrategy getAllowedEndpointsForRoleStrategy;
  
  @Override
  public IListModel<IEndpointModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllowedEndpointsForRoleStrategy.execute(dataModel);
  }
}
