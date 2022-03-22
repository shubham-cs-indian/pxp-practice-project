package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetChildMasterTaxonomiesForTaxonomyLevelStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<String>> {
  
}
