package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTranslatableAssetInstance extends IRuntimeInteractor<IAssetInstanceSaveModel, IGetKlassInstanceModel> {
  
}
