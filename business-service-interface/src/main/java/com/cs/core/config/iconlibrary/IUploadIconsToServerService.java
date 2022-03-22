package com.cs.core.config.iconlibrary;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;

public interface IUploadIconsToServerService extends ISaveConfigService<IUploadMultipleIconsRequestModel, IBulkUploadResponseAssetModel> {
  
}
