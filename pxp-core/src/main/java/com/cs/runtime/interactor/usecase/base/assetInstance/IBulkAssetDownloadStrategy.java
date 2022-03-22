package com.cs.runtime.interactor.usecase.base.assetInstance;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadStrategyResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IBulkAssetDownloadStrategy extends
    IRuntimeStrategy<IBulkAssetDownloadRequestModel, IBulkAssetDownloadStrategyResponseModel> {
}
