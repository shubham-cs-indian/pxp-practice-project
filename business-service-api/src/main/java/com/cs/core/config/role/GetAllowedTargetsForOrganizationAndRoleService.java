package com.cs.core.config.role;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;
import com.cs.core.config.strategy.usecase.role.IGetAllowedTargetsForOrganizationAndRoleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllowedTargetsForOrganizationAndRoleService extends AbstractGetConfigService<IGetAllowedTargetsForRoleRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetAllowedTargetsForOrganizationAndRoleService {
  
  @Autowired
  IGetAllowedTargetsForOrganizationAndRoleStrategy getAllowedTargetsForRole;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(IGetAllowedTargetsForRoleRequestModel dataModel)
      throws Exception
  {
    
    return getAllowedTargetsForRole.execute(dataModel);
  }
}
