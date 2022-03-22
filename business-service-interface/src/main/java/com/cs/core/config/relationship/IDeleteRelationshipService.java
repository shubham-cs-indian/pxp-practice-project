package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteRelationshipService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
