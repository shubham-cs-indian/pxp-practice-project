package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllChildMajorTaxonomiesByParentIdStrategy extends IConfigStrategy<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}