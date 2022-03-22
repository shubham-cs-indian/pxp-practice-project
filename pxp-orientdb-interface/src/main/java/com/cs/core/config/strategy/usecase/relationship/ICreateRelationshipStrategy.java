package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateRelationshipStrategy
    extends IConfigStrategy<ICreateRelationshipModel, IGetRelationshipModel> {
  
}
