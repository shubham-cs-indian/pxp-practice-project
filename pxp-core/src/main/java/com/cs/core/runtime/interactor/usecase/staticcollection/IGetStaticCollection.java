package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetStaticCollection  extends
IRuntimeInteractor<IGetKlassInstanceTreeStrategyModel, IGetStaticCollectionModel> {
  
}
