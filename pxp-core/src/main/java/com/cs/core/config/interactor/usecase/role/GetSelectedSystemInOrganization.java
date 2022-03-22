package com.cs.core.config.interactor.usecase.role;

import com.cs.core.config.role.IGetSelectedSystemInOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.config.strategy.usecase.role.IGetSelectedSystemInOrganizationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSelectedSystemInOrganization
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<ISystemsVsEndpointsModel>>
    implements IGetSelectedSystemInOrganization {
  
  @Autowired
  protected IGetSelectedSystemInOrganizationService getSelectedSystemInOrganizationService;
  
  @Override
  public IListModel<ISystemsVsEndpointsModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSelectedSystemInOrganizationService.execute(dataModel);
  }
}
