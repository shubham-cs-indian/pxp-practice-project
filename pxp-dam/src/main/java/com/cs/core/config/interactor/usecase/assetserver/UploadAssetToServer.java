package com.cs.core.config.interactor.usecase.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.assetserver.IUploadMultipleAssetsToServerService;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;

@Service
public class UploadAssetToServer extends AbstractSaveConfigInteractor<IUploadAssetModel, IBulkUploadResponseAssetModel>
    implements IUploadMultipleAssetsToServer {
  
  @Autowired
  protected IUploadMultipleAssetsToServerService uploadAssetToServerService;
  
  @Override
  public IBulkUploadResponseAssetModel executeInternal(IUploadAssetModel model) throws Exception
  {
    return uploadAssetToServerService.execute(model);
  }
}
