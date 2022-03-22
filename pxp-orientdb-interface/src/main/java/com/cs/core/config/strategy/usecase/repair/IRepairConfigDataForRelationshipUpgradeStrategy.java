package com.cs.core.config.strategy.usecase.repair;

import com.cs.core.config.interactor.model.relationship.IRelationshipInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRepairConfigDataForRelationshipUpgradeStrategy
    extends IConfigStrategy<IModel, IRelationshipInfoModel> {
  
}
