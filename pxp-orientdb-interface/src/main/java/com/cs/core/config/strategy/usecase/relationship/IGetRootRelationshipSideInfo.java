package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRootRelationshipSideInfo
    extends IConfigStrategy<IIdParameterModel, IGetRelationshipModel> {
  
}
