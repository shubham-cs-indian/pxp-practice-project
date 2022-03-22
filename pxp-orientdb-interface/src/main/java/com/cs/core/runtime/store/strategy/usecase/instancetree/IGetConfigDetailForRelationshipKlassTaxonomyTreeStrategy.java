package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailForRelationshipKlassTaxonomyTreeStrategy
    extends IRuntimeStrategy<IModuleEntitiesWithUserIdModel, IConfigDetailsKlassTaxonomyTreeResponseModel> {
  
}
