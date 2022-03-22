package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetChildKlassIdsInHierarchyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetChildKlassIdsInHierarchyStrategy
    extends IConfigStrategy<IIdsListParameterModel, IGetChildKlassIdsInHierarchyModel> {
  
}
