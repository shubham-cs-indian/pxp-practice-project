package com.cs.core.config.interactor.usecase.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.assetserver.IUploadMultipleAssetsInForegroundService;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

/**
 * This is service layer to upload multiple assets and create asset instance in foreground.
 * @author pranav.huchche
 *
 */
@Service("uploadMultipleAssetsInForeground")
public class UploadMultipleAssetsInForeground extends AbstractRuntimeInteractor<IUploadAssetModel, IBulkUploadResponseAssetModel>
    implements IUploadMultipleAssetsInForeground {
  
  @Autowired
  protected IUploadMultipleAssetsInForegroundService uploadMultipleAssetsInForegroundService;

  
  
  @Override
  protected IBulkUploadResponseAssetModel executeInternal(IUploadAssetModel model) throws Exception
  {
    return uploadMultipleAssetsInForegroundService.execute(model);
  }
}
