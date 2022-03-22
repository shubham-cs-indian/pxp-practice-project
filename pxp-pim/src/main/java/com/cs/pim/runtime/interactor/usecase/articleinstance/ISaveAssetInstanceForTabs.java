package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveAssetInstanceForTabs
    extends IRuntimeInteractor<IAssetInstanceSaveModel, IGetKlassInstanceModel> {
  
}
