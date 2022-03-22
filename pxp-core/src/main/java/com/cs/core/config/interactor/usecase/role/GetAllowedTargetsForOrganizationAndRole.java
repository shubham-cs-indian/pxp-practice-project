package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;
import com.cs.core.config.role.IGetAllowedTargetsForOrganizationAndRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllowedTargetsForOrganizationAndRole extends
    AbstractGetConfigInteractor<IGetAllowedTargetsForRoleRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetAllowedTargetsForOrganizationAndRole {
  
  @Autowired
  protected IGetAllowedTargetsForOrganizationAndRoleService getAllowedTargetsForOrganizationAndRoleService;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(IGetAllowedTargetsForRoleRequestModel dataModel)
      throws Exception
  {
    
    return getAllowedTargetsForOrganizationAndRoleService.execute(dataModel);
  }
}
