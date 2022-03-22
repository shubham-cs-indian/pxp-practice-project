package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;

public interface IGetConfigDetailsForRelationshipQuicklistStrategy 
  extends IConfigStrategy<IConfigDetailsForRelationshipQuicklistRequestModel, IConfigDetailsForRelationshipQuicklistResponseModel> {
  
}
