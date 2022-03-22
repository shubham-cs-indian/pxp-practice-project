package com.cs.core.config.interactor.usecase.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.iconlibrary.IUploadIconsToServerService;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;

/**
 * Service layer implementation for bulk icon upload
 * 
 * @author pranav.huchche
 *
 */
@Service
public class UploadIconsToServer extends AbstractSaveConfigInteractor<IUploadMultipleIconsRequestModel, IBulkUploadResponseAssetModel>
    implements IUploadIconsToServer {
  
  @Autowired
  protected IUploadIconsToServerService uploadIconsToServerService;
  
  @Override
  protected IBulkUploadResponseAssetModel executeInternal(IUploadMultipleIconsRequestModel model) throws Exception
  {
    return uploadIconsToServerService.execute(model);
  }
}
