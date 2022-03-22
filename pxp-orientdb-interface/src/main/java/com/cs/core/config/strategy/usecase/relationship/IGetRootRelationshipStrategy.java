package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;

public interface IGetRootRelationshipStrategy
    extends IConfigStrategy<IIdParameterModel, IKlassRelationshipSidesInfoModel> {
  
}
