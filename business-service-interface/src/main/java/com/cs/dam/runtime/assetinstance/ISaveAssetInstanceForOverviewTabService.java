package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ISaveAssetInstanceForOverviewTabService
    extends IRuntimeService<IAssetInstanceSaveModel, IGetKlassInstanceModel> {
}
