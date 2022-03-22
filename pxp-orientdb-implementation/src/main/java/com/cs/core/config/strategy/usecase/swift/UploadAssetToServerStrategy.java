package com.cs.core.config.strategy.usecase.swift;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.AssetServerUploadResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetServerUploadResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;

@Component
public class UploadAssetToServerStrategy implements IUploadAssetToServerStrategy {
  
  public static final String    REQUEST_HEADER_AUTH_TOKEN = "X-Auth-Token";
  
  /*@Autowired
  ISetAssetUploadStatusStrategy     setAssetUploadStatusStrategy;*/
  
  @Value("${system.mode}")
  protected String              mode;
  
  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String> assetUploadStatusMap;
  
  @Override
  public IAssetServerUploadResponseModel execute(IAssetUploadDataModel model) throws Exception
  {
    String storageUrl = model.getStorageUrl();
    
    String container = model.getContainer();
    String assetKey = model.getAssetKey();
    byte[] assetBytes = model.getAssetBytes();
    Map<String, String> assetDataMap = model.getAssetDataMap();
    
    URL uri = new URL(storageUrl + "/" + container + "/" + assetKey);

    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      String message = "==== SWIFT SERVER REQUEST==== URI - " + uri;
      RDBMSLogger.instance().info(message);
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println("====================== SWIFT SERVER REQUEST====================");
      System.out.println("\nURI : " + uri);
    }
    
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) uri.openConnection();
      connection.setRequestMethod("PUT");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setFixedLengthStreamingMode(assetBytes.length);
      for (String mapKey : assetDataMap.keySet()) {
        connection.setRequestProperty(mapKey, assetDataMap.get(mapKey));
      }
      
      DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
      dataOutputStream.write(assetBytes);
      dataOutputStream.close();
      
      int responseCode = connection.getResponseCode();
      
      if (responseCode == 201) {
        // set asset upload status to success
        // setAssetUploadStatusAsSuccess(assetKey);
        return new AssetServerUploadResponseModel(responseCode);
        
      }
      else if (responseCode == 401) {
        return new AssetServerUploadResponseModel(responseCode);
      }
      else {
        // set asset upload status as faiied after exception
        // setAssetUploadStatusAsFailure(assetKey);
        PluginException exception = new PluginException();
        exception.getDevExceptionDetails()
            .get(0)
            .setDetailMessage("Upload Asset Failed with Response Code: " + responseCode
                + ", Message: " + connection.getResponseMessage());
        throw exception;
      }
    }
    finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
  
  /*private void setAssetUploadStatusAsFailure(String assetKey) throws Exception
  {
    assetUploadStatusMap.put(assetKey, "-1");
    IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
    assetUploadStatus.setId(assetKey);
    assetUploadStatus.setProgress("0");
    assetUploadStatus.setStatus("Failed");
    setAssetUploadStatusStrategy.execute(assetUploadStatus);
  }
  
  private void setAssetUploadStatusAsSuccess(String assetKey) throws Exception
  {
    assetUploadStatusMap.remove(assetKey);
    IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
    assetUploadStatus.setId(assetKey);
    assetUploadStatus.setProgress("100");
    assetUploadStatus.setStatus("success");
    setAssetUploadStatusStrategy.execute(assetUploadStatus);
  }*/
}
