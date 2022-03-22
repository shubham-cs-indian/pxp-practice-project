package com.cs.core.runtime.instancetree;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;

public interface IGetDefaultTypesService extends IGetConfigService<IDefaultTypesRequestModel, IListModel<IGetDefaultTypesResponseModel>> {
  
}
