package com.cs.core.config.strategy.usecase.historyversions;

import com.cs.core.config.interactor.model.historyversions.IKlassesAndTaxonomiesForVersionsInfoRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;

public interface IGetKlassAndTaxonomyInfoForVersionsStrategy extends
    IConfigStrategy<IKlassesAndTaxonomiesForVersionsInfoRequestModel, IGetKlassInstanceVersionsForComparisonModel> {
  
}
