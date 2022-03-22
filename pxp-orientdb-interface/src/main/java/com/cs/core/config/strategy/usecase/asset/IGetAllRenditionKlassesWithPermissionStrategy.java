package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;

public interface IGetAllRenditionKlassesWithPermissionStrategy
    extends IConfigStrategy<IIdsListWithUserModel, IBulkDownloadConfigInformationResponseModel> {
  
}
