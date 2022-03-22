package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteStaticCollection
extends IRuntimeInteractor<IIdsListParameterModel, IBulkResponseModel> {
  
}