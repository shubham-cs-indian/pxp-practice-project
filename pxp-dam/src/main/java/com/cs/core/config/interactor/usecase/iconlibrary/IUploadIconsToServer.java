package com.cs.core.config.interactor.usecase.iconlibrary;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;

public interface IUploadIconsToServer extends ISaveConfigInteractor<IUploadMultipleIconsRequestModel, IBulkUploadResponseAssetModel> {
  
}
