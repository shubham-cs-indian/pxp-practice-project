package com.cs.core.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssetInstanceForCustomTab
    extends IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
}
