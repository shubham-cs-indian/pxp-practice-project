package com.cs.core.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssetInstanceToExport
    extends IRuntimeInteractor<IAssetInstanceExportRequestModel, IAssetInstanceExportResponseModel> {
}
