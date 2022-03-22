package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;

public interface IGetAllRelationshipsService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllRelationshipsResponseModel> {
  
}
