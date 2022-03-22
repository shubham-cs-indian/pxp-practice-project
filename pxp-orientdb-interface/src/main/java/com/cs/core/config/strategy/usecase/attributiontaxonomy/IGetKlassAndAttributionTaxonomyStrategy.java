package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetKlassAndAttributionTaxonomyStrategy
    extends IConfigStrategy<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
