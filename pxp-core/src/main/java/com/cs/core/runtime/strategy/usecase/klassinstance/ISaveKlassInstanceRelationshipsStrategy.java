package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceResponseModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ISaveKlassInstanceRelationshipsStrategy extends
    IRuntimeStrategy<ISaveRelationshipInstanceStrategyModel, ISaveRelationshipInstanceResponseModel> {
  
}
