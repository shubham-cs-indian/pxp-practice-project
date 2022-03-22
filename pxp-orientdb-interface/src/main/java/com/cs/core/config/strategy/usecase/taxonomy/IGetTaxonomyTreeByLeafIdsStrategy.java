package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetTaxonomyTreeByLeafIdsStrategy
    extends IRuntimeStrategy<IGetTaxonomyTreeLeafIdsStrategyModel, ICategoryInformationModel> {
  
}
