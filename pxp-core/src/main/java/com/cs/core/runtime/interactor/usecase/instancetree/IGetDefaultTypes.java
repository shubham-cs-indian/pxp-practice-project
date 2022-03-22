package com.cs.core.runtime.interactor.usecase.instancetree;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;

public interface IGetDefaultTypes extends
    IGetConfigInteractor<IDefaultTypesRequestModel, IListModel<IGetDefaultTypesResponseModel>> {
  
}
