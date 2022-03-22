package com.cs.core.config.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.AssetUploadSuccessResponseModel;
import com.cs.core.config.interactor.model.asset.BulkCreateAssetInstanceRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadSuccessResponseModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceRequestModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.runtime.interactor.model.assetupload.IUploadAssetResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.dam.runtime.assetinstance.IBulkCreateAssetInstanceService;
import com.cs.dam.runtime.usecase.upload.IUploadAssetsService;

/**
 * This is service layer to upload multiple assets and create asset instance in
 * foreground.
 * 
 * @author pranav.huchche
 *
 */
@Service("uploadMultipleAssetsInForegroundService")
public class UploadMultipleAssetsInForegroundService extends AbstractRuntimeService<IUploadAssetModel, IBulkUploadResponseAssetModel>
    implements IUploadMultipleAssetsInForegroundService {
  
  @Autowired
  protected IFetchAssetConfigurationDetails fetchAssetConfigurationDetails;
  
  @Autowired
  protected PermissionUtils                 permissionUtils;
  
  @Autowired
  TransactionThreadData                     transactionThreadData;
  
  @Autowired
  protected IUploadAssetsService            uploadAssetsService;
  
  @Autowired
  IBulkCreateAssetInstanceService           bulkCreateAssetInstanceService;
  
  @Override
  protected IBulkUploadResponseAssetModel executeInternal(IUploadAssetModel model) throws Exception
  {
    // Upload asset to swift server
    IUploadAssetResponseModel uploadResponseModel = uploadAssetsService.execute(model);
    
    // Create Asset Instances
    IBulkCreateAssetInstanceRequestModel dataModel = new BulkCreateAssetInstanceRequestModel();
    dataModel.setAssetKeysModelList(uploadResponseModel.getAssetKeysModelList());
    IBulkCreateAssetInstanceResponseModel informationModel = bulkCreateAssetInstanceService.execute(dataModel);
    
    // Create response model
    IBulkUploadResponseAssetModel responseModel = new BulkUploadResponseAssetModel();
    IAssetUploadSuccessResponseModel successModel = new AssetUploadSuccessResponseModel();
    successModel.setSuccessIIds(informationModel.getSuccessInstanceIIds());
    responseModel.setFailure(uploadResponseModel.getFailure());
    responseModel.setSuccess(successModel);
    
    return responseModel;
  }
}
