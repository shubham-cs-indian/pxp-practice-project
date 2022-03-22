package com.cs.dam.runtime.interactor.version;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArchivedAssetInstanceForVersionTab extends
    IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
