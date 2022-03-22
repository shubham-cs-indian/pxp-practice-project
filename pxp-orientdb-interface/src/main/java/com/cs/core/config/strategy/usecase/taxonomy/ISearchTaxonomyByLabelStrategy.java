package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.ITaxonomySearchStrategyModel;

public interface ISearchTaxonomyByLabelStrategy
    extends IConfigStrategy<ITaxonomySearchStrategyModel, IListModel<ICategoryInformationModel>> {
  
}
