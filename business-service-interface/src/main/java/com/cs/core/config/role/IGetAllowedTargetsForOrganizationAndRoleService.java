package com.cs.core.config.role;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;

public interface IGetAllowedTargetsForOrganizationAndRoleService extends
    IGetConfigService<IGetAllowedTargetsForRoleRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}
