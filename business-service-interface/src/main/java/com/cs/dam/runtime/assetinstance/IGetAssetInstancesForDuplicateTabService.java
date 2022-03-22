package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceDuplicateTabResponseModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;

public interface IGetAssetInstancesForDuplicateTabService
    extends IRuntimeService<IGetInstanceRequestModel, IAssetInstanceDuplicateTabResponseModel> {
  
}
