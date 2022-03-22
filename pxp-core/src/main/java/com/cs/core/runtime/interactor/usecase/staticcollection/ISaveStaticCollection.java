package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveStaticCollection extends
IRuntimeInteractor<ISaveStaticCollectionModel, IGetStaticCollectionModel> {
  
}
