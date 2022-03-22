package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeletePropertyCollectionService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
