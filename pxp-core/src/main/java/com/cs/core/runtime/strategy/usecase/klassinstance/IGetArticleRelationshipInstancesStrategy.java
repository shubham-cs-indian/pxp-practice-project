package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.relationship.IGetAllRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.IGetAllRelationshipsModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetArticleRelationshipInstancesStrategy
    extends IRuntimeStrategy<IGetAllRelationshipsModel, IGetAllRelationshipInstancesModel> {
  
}
