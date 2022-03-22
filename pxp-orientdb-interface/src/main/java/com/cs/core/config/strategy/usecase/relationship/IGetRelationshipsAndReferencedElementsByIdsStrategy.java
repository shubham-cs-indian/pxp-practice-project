package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.IGetReferencedRelationshipsAndElementsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;

public interface IGetRelationshipsAndReferencedElementsByIdsStrategy
    extends IConfigStrategy<IIdsListWithUserModel, IGetReferencedRelationshipsAndElementsModel> {
  
}
