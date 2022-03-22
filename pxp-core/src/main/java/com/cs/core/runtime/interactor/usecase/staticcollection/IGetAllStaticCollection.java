package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllStaticCollection extends
IRuntimeInteractor<IIdParameterModel, IListModel<ICollectionModel>> {
  
}
