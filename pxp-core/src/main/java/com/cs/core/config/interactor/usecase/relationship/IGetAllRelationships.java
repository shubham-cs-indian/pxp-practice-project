package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;

public interface IGetAllRelationships
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllRelationshipsResponseModel> {
  
}
