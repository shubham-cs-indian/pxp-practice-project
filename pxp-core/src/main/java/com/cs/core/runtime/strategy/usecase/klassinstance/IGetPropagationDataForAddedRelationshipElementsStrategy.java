package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetPropagationDataForAddedRelationshipElementsStrategy extends
    IRuntimeStrategy<IRelationshipPropertiesToInheritModel, IValueInheritancePropagationModel> {
  
}
