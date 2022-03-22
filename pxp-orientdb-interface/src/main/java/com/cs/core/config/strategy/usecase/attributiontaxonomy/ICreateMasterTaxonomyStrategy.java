package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateMasterTaxonomyStrategy
    extends IConfigStrategy<ICreateMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
