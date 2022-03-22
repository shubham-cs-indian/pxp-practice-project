package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetEligibleRelationshipsForRelationshipInheritanceService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> {
  
}
