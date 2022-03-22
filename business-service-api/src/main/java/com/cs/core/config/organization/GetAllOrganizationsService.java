package com.cs.core.config.organization;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.organization.IGetAllOrganizationsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllOrganizationsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllOrganizationResponseModel>
    implements IGetAllOrganizationsService {
  
  @Autowired
  protected IGetAllOrganizationsStrategy getAllOrganizationsStrategy;
  
  @Override
  public IGetAllOrganizationResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllOrganizationsStrategy.execute(dataModel);
  }
}
