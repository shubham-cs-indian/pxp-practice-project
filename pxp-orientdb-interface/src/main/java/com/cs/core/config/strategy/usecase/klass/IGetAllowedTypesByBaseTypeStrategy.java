package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

public interface IGetAllowedTypesByBaseTypeStrategy
    extends IConfigStrategy<IAllowedTypesRequestModel, ITaxonomyHierarchyModel> {
  
}
