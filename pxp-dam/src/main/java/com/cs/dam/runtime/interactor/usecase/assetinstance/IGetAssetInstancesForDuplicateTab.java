package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceDuplicateTabResponseModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssetInstancesForDuplicateTab
    extends IRuntimeInteractor<IGetInstanceRequestModel, IAssetInstanceDuplicateTabResponseModel> {
  
}
