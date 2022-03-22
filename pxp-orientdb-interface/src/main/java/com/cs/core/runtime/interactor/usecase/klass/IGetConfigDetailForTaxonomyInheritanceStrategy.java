package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetConfigDetailForTaxonomyInheritanceStrategy
extends IConfigStrategy<IIdsListParameterModel, IGetInheritanceTaxonomyIdsResponseModel> {
  
}
