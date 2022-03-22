package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;

public interface IGetAllowedTargetsForOrganizationAndRole extends
    IGetConfigInteractor<IGetAllowedTargetsForRoleRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}
