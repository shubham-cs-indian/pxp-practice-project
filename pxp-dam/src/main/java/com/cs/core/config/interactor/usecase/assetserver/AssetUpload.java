package com.cs.core.config.interactor.usecase.assetserver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.Constants;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IAssetServerUploadResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.config.strategy.usecase.swift.IUploadAssetToServerStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.utils.dam.AssetUtils;

@Service
public class AssetUpload extends AbstractRuntimeInteractor<IAssetUploadModel, IModel>
    implements IAssetUpload {
  
  @Autowired
  IAssetServerDetailsModel         assetServerDetails;
  
  @Autowired
  IUploadAssetToServerStrategy     uploadAssetToServerStrategy;
  
  @Autowired
  IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;
  
  @Override
  public IModel executeInternal(IAssetUploadModel dataModel) throws Exception
  {
    try {
      IAssetUploadDataModel uploadDataModel = dataModel.getAssetUploadDataModel();
      IAssetServerUploadResponseModel strategyResult = uploadAssetToServerStrategy
          .execute(uploadDataModel);
      Integer responseCode = strategyResult.getResponseCode();

      RDBMSLogger.instance().info("Upload Response : " + responseCode);
      if (responseCode == 401) {
        RDBMSLogger.instance().info("Reauthenticate");
        reAuthenticateAndUpload(uploadDataModel, uploadDataModel.getAssetDataMap());
      }
    }
    finally {
      AssetUtils.deleteFileAndDirectory(dataModel.getAssetSourcePath());
    }
    
    return null;
  }
  
  private void reAuthenticateAndUpload(IAssetUploadDataModel uploadDataModel,
      Map<String, String> thumbDataMap) throws Exception
  {
    IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy
        .execute(null);
    assetServerDetails = assetServerDetailsFromStrategy;
    uploadDataModel.setStorageUrl(assetServerDetails.getStorageURL());
    thumbDataMap.put(Constants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.getAuthToken());
    uploadDataModel.setAssetDataMap(thumbDataMap);
    uploadAssetToServerStrategy.execute(uploadDataModel);
  }
}
