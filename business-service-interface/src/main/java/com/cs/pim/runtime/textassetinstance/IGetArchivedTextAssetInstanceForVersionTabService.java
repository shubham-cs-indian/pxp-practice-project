package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;

public interface IGetArchivedTextAssetInstanceForVersionTabService extends
    IRuntimeService<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
