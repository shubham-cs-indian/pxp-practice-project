package com.cs.core.config.role;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.config.strategy.usecase.role.IGetSelectedSystemInOrganizationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSelectedSystemInOrganizationService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<ISystemsVsEndpointsModel>>
    implements IGetSelectedSystemInOrganizationService {
  
  @Autowired
  protected IGetSelectedSystemInOrganizationStrategy getSelectedSystemInOrganizationStrategy;
  
  @Override
  public IListModel<ISystemsVsEndpointsModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSelectedSystemInOrganizationStrategy.execute(dataModel);
  }
}
