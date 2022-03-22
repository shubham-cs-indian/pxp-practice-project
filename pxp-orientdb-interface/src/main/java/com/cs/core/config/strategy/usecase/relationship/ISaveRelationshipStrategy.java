package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipStrategyResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveRelationshipStrategy
    extends IConfigStrategy<ISaveRelationshipModel, ISaveRelationshipStrategyResponseModel> {
  
}
