package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IGetGovernanceRulesByKlassAndTaxonomyIdsStrategy extends
    IConfigStrategy<IMulticlassificationRequestModel, IListModel<IGetKeyPerformanceIndexModel>> {
  
}
