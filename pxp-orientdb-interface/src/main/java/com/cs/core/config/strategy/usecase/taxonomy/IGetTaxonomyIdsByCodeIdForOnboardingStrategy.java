package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetTaxonomyIdsByCodeIdForOnboardingStrategy
    extends IConfigStrategy<IListModel<String>, IIdsListParameterModel> {
  
}
