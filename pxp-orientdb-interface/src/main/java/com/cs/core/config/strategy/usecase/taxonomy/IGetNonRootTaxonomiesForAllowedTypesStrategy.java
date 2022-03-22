package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

public interface IGetNonRootTaxonomiesForAllowedTypesStrategy
    extends IConfigStrategy<IAllowedTypesRequestModel, ITaxonomyHierarchyModel> {
  
}
