package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetTaxonomyHierarchyStrategy
    extends IRuntimeStrategy<IIdParameterModel, ITaxonomyInformationModel> {
  
}
