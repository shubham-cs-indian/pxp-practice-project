package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllRelationshipsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllRelationshipsResponseModel> {
  
}
