package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.businessapi.endpoint.IGetAllowedEndpointsForRoleService;

@Service
public class GetAllowedEndpointsForRole
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IEndpointModel>>
    implements IGetAllowedEndpointsForRole {
  
  @Autowired
  protected IGetAllowedEndpointsForRoleService getAllowedEndpointsForRoleService;
  
  @Override
  public IListModel<IEndpointModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllowedEndpointsForRoleService.execute(dataModel);
  }
}
