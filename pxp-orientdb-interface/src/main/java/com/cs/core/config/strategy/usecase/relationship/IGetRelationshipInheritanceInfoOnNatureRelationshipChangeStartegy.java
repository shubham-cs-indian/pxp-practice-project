package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy extends IConfigStrategy<IIdsListParameterModel, IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel>{
  
}
