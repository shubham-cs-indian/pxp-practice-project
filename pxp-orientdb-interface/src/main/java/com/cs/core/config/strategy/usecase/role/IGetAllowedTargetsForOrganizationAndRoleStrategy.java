package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllowedTargetsForOrganizationAndRoleStrategy
    extends IConfigStrategy<IGetAllowedTargetsForRoleRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}
