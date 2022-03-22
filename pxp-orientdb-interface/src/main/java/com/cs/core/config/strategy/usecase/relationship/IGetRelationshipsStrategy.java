package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRelationshipsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IRelationship>> {
  
}
