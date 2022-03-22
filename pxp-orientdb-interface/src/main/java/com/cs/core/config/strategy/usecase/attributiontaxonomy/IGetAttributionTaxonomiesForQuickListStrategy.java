package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAttributionTaxonomiesForQuickListStrategy
    extends IConfigStrategy<IIdsListParameterModel, IGetAttributionTaxonomyModel> {
  
}
