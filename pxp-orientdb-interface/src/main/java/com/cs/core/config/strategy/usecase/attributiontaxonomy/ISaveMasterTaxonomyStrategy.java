package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveMasterTaxonomyStrategy extends
    IConfigStrategy<ISaveMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPStrategyResponseModel> {
  
}
