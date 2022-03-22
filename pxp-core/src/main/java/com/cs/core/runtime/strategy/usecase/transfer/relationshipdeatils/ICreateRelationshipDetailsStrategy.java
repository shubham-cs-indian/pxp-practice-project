package com.cs.core.runtime.strategy.usecase.transfer.relationshipdeatils;

import com.cs.core.config.interactor.model.processdetails.IProcessRelationshipDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateRelationshipDetailsStrategy
    extends IConfigStrategy<IProcessRelationshipDetailsModel, IModel> {
  
}
