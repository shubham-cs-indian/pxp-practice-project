package com.cs.pim.runtime.interactor.usecase.dynamiccollection;


import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllDynamicCollection extends
    IRuntimeInteractor<IIdParameterModel, IListModel<ICollectionModel>> {
  
}
