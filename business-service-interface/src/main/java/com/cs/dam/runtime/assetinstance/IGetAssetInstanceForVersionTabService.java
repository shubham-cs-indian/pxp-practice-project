package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;

public interface IGetAssetInstanceForVersionTabService
    extends IRuntimeService<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
