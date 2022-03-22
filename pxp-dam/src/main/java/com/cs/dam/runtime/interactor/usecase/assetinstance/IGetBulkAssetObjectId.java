package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetBulkAssetObjectId extends IRuntimeInteractor<IBulkAssetDownloadWithVariantsModel, IBulkAssetDownloadResponseModel> {
  
}
