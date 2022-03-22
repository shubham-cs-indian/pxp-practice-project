package com.cs.core.runtime.staticcollection;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteStaticCollectionService
extends IRuntimeService<IIdsListParameterModel, IBulkResponseModel> {
  
}