package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;


public interface IGetAssetShareDialogInformationConfigDetailsStrategy
    extends IConfigStrategy<IIdsListWithUserModel, IAssetShareDialogInformationModel> {
  
}
