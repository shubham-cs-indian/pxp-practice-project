package com.cs.pim.runtime.dynamiccollection;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteDynamicCollectionService extends IRuntimeService<IIdsListParameterModel, IBulkResponseModel> {
  
}