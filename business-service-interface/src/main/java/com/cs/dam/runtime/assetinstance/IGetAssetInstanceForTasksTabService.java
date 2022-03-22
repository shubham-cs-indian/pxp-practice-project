package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;

public interface IGetAssetInstanceForTasksTabService extends IRuntimeService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel> {
  
}
