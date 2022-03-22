package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetImmediateChildMajorTaxonomiesByParentIdStrategy extends
    IConfigStrategy<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}
