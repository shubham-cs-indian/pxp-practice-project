package com.cs.core.config.interactor.usecase.assetserver;

import java.util.Map;

import javax.annotation.Resource;

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
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.strategy.usecase.assetstatus.ISetAssetUploadStatusStrategy;
import com.cs.dam.asset.processing.VideoAssetConverter;
import com.cs.utils.dam.AssetUtils;

@Scope("prototype")
@Component
public class VideoConvertAndUploadTask implements Runnable {
  
  public static final String                 REQUEST_HEADER_AUTH_TOKEN = "X-Auth-Token";
  
  @Autowired
  protected IAssetServerDetailsModel         assetServerDetails;
  
  @Autowired
  protected IUploadAssetToServerStrategy     uploadAssetToServerStrategy;
  
  @Autowired
  protected IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;
  
  protected String                           videoSourcePath;
  
  protected IAssetUploadDataModel            uploadDataModel;
  
  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String>              assetUploadStatusMap;
  @Autowired
  ISetAssetUploadStatusStrategy              setAssetUploadStatusStrategy;
  
  public VideoConvertAndUploadTask(String videoSourcePath, IAssetUploadDataModel uploadDataModel)
  {
    this.videoSourcePath = videoSourcePath;
    this.uploadDataModel = uploadDataModel;
  }
  
  @Override
  public void run()
  {
    try {
      byte[] mp4VideoBytes = VideoAssetConverter.convertVideo(videoSourcePath);
      uploadDataModel.setAssetBytes(mp4VideoBytes);
      
      setAssetUploadStatus();
      
      IAssetServerUploadResponseModel strategyResult = uploadAssetToServerStrategy
          .execute(uploadDataModel);
      Integer responseCode = strategyResult.getResponseCode();
      
      if (responseCode == 401) {
        reAuthenticateAndUpload(uploadDataModel, uploadDataModel.getAssetDataMap());
      }
    }
    catch (Exception e) {
      assetUploadStatusMap.put(uploadDataModel.getAssetKey(), "-1");
      RDBMSLogger.instance().exception(e);
    }
    finally {
      AssetUtils.deleteFileAndDirectory(videoSourcePath);
    }
  }
  
  private void setAssetUploadStatus() throws Exception
  {
    String assetKey = uploadDataModel.getAssetKey();
    assetUploadStatusMap.put(assetKey, "50");
    IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
    assetUploadStatus.setId(assetKey);
    assetUploadStatus.setProgress("50");
    assetUploadStatus.setStatus("InProgress");
    setAssetUploadStatusStrategy.execute(assetUploadStatus);
  }
  
  private void reAuthenticateAndUpload(IAssetUploadDataModel uploadDataModel,
      Map<String, String> thumbDataMap) throws Exception
  {
    IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy
        .execute(null);
    assetServerDetails = assetServerDetailsFromStrategy;
    uploadDataModel.setStorageUrl(assetServerDetails.getStorageURL());
    thumbDataMap.put(REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.getAuthToken());
    uploadDataModel.setAssetDataMap(thumbDataMap);
    uploadAssetToServerStrategy.execute(uploadDataModel);
  }
}
