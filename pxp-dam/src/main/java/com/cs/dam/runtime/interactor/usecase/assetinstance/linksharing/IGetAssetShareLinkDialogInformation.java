package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssetShareLinkDialogInformation extends
    IRuntimeInteractor<IIdsListParameterModel, IAssetShareDialogInformationModel> {
  
}
