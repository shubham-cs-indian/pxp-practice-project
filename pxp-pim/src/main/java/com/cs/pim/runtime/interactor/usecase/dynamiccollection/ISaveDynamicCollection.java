package com.cs.pim.runtime.interactor.usecase.dynamiccollection;


import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IGetDynamicCollectionModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveDynamicCollection extends
    IRuntimeInteractor<IDynamicCollectionModel, IGetDynamicCollectionModel> {
  
}
