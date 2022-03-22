package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetSelectedKlassesAndTaxonomiesByIdsResponseModel;


public interface IGetKlassesAndTaxonomyByIds extends IConfigStrategy<IGetKlassTaxonomyTreeRequestModel, IGetSelectedKlassesAndTaxonomiesByIdsResponseModel> {
  
}
