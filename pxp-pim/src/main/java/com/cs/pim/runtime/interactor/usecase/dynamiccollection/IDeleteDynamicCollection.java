package com.cs.pim.runtime.interactor.usecase.dynamiccollection;


import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteDynamicCollection
    extends IRuntimeInteractor<IIdsListParameterModel, IBulkResponseModel> {
  
}