package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassesForRelationshipSideStrategy
    extends IConfigStrategy<IIdParameterModel, IGetKlassesForRelationshipModel> {
  
}
