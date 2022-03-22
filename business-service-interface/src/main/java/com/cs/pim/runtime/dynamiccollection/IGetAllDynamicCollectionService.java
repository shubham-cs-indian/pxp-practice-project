package com.cs.pim.runtime.dynamiccollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllDynamicCollectionService extends IRuntimeService<IIdParameterModel, IListModel<ICollectionModel>> {
  
}
