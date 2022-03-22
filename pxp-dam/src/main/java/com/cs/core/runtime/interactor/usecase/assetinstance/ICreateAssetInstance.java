package com.cs.core.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateAssetInstance
    extends IRuntimeInteractor<ICreateAssetInstanceModel, IKlassInstanceInformationModel> {
}
