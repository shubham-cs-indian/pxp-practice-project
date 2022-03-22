package com.cs.core.config.interactor.usecase.assetserver;

import java.util.Map;

import javax.annotation.Resource;

import com.cs.core.rdbms.driver.RDBMSLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IAssetServerUploadResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.interactor.model.assetstatus.AssetUploadStatusModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.config.strategy.usecase.swift.IUploadAssetToServerStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.utils.dam.AssetUtils;

@Scope("prototype")
@Component
public class AssetUploadTask implements Runnable {
  
  @Autowired
  public String                    inddServerProtocol;
  @Autowired
  public String                    inddServerHost;
  @Autowired
  public String                    inddServerPort;
  @Autowired
  public String                    inddJsxFilePathOnServer;
  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String>    assetUploadStatusMap;
  @Autowired
  IAssetServerDetailsModel         assetServerDetails;
  @Autowired
  IUploadAssetToServerStrategy     uploadAssetToServerStrategy;
  @Autowired
  IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;

  @Autowired
  String                           assetServerAuthURL;
  @Resource(name = "assetStoreAuthenticationMap")
  Map<String, String>              assetStoreAuthenticationMap;
  private IAssetUploadDataModel    uploadDataModel;
  private String                   sourcePath;
  
  public AssetUploadTask(String sourcePath, IAssetUploadDataModel uploadDataModel)
  {
    this.sourcePath = sourcePath;
    this.uploadDataModel = uploadDataModel;
  }  
  
  @Override
  public void run()
  {
    try {
      RDBMSLogger.instance().info("Started Upload");
      // setAssetUploadStatus();
      
      IAssetServerUploadResponseModel strategyResult = uploadAssetToServerStrategy
          .execute(uploadDataModel);
      Integer responseCode = strategyResult.getResponseCode();
      RDBMSLogger.instance().info("Upload Response : " + responseCode);
      if (responseCode == 401) {
        RDBMSLogger.instance().info("Reauthenticate");
        reAuthenticateAndUpload(uploadDataModel, uploadDataModel.getAssetDataMap());
      }
      
      Map<String, String> assetDataMap = uploadDataModel.getAssetDataMap();
      String extension = assetDataMap.get(Constants.REQUEST_HEADER_OBJECT_META_FORMAT);      
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Failed");
      String assetKey = uploadDataModel.getAssetKey();
      assetUploadStatusMap.put(assetKey, "-1");
      IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
      assetUploadStatus.setId(assetKey);
      assetUploadStatus.setStatus("Failed");
      try {
        // FIXME: Asset Upload Status Issue Resolution
        /* setAssetUploadStatusStrategy.execute(assetUploadStatus);*/
      }
      catch (Exception e1) {
        RDBMSLogger.instance().exception(e1);
      }
      RDBMSLogger.instance().exception(e);
    }
    finally {
      AssetUtils.deleteFileAndDirectory(sourcePath);
    }
  }
  
  private void setAssetUploadStatus() throws Exception
  {
    String assetKey = uploadDataModel.getAssetKey();
    assetUploadStatusMap.put(assetKey, "50");
    
    IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
    assetUploadStatus.setId(assetKey);
    assetUploadStatus.setStatus("InProgress");
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
