package com.cs.core.config.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.asset.AssetUploadSuccessResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadSuccessResponseModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithGlobalPermissionStrategy;
import com.cs.core.runtime.interactor.model.assetupload.IUploadAssetResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.dam.runtime.usecase.upload.IUploadAssetsService;

@Service("uploadAssetToServerService")
public class UploadAssetToServerService extends AbstractSaveConfigService<IUploadAssetModel, IBulkUploadResponseAssetModel>
    implements IUploadMultipleAssetsToServerService {
  
  @Autowired
  protected IGetAssetWithGlobalPermissionStrategy getAssetWithGlobalPermissionStrategy;
  
  @Autowired
  protected IFetchAssetConfigurationDetails       fetchAssetConfigurationDetails;
  
  @Autowired
  TransactionThreadData                           transactionThreadData;
  
  @Autowired
  protected IUploadAssetsService                  uploadAssetsService;
  
  @Override
  public IBulkUploadResponseAssetModel executeInternal(IUploadAssetModel model) throws Exception
  {
    // Upload asset to swift server
    IUploadAssetResponseModel uploadResponseModel = uploadAssetsService.execute(model);
    
    // Create response model
    IAssetUploadSuccessResponseModel successModel = new AssetUploadSuccessResponseModel();
    successModel.setAssetKeysModelList(uploadResponseModel.getAssetKeysModelList());
    successModel.setDuplicateId(uploadResponseModel.getDuplicateIId());
    IBulkUploadResponseAssetModel responseModel = new BulkUploadResponseAssetModel();
    responseModel.setFailure(uploadResponseModel.getFailure());
    responseModel.setSuccess(successModel);
    
    return responseModel;
  }
}
