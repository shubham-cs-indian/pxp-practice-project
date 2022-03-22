package com.cs.core.config.strategy.usecase.swift;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.model.assetstatus.AssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.assetserver.AssetObjectNotFoundException;
import com.cs.core.runtime.interactor.exception.assetstatus.AssetUploadInProgressException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;

@Component
public class GetAssetFromServerStrategy implements IGetAssetFromServerStrategy {
  
  public static final String    REQUEST_HEADER_AUTH_TOKEN          = "X-Auth-Token";
  
  /*@Autowired
  protected IElasticGetAssetUploadStatusStrategy getAssetUploadStatusStrategy;*/
  public static final String    RESPONSE_HEADER_OBJECT_META_FORMAT = "X-Object-Meta-Format";
  public static final String    RESPONSE_HEADER_OBJECT_META_NAME   = "X-Object-Meta-Name";
  
  @Value("$(system.mode)")
  protected String              mode;
  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String> assetUploadStatusMap;
  
  @Override
  public IGetAssetDetailsStrategyModel execute(IGetAssetDetailsRequestModel model) throws Exception
  {
    String assetKey = model.getAssetKey();
    // checkAssetUploadStatus(model, assetKey);
    // get asset from swift
    URL uri = new URL(model.getAssetServerDetails()
        .getStorageURL() + "/" + model.getContainer() + "/" + assetKey);
    
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      String message = "==== SWIFT SERVER REQUEST==== URI - " + uri;
      RDBMSLogger.instance().info(message);
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println("====================== SWIFT SERVER REQUEST====================");
      System.out.println("\nURI : " + uri);
    }
    
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("GET");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty(REQUEST_HEADER_AUTH_TOKEN, model.getAssetServerDetails()
        .getAuthToken());
    Map<String, String> requestHeaders = model.getRequestHeaders();
    if (requestHeaders != null) {
      for (String headerName : requestHeaders.keySet()) {
        connection.setRequestProperty(headerName, requestHeaders.get(headerName));
      }
    }
    
    connection.connect();
    
    int responseCode = connection.getResponseCode();
    
    if (responseCode == 200 || responseCode == 206) {
      InputStream inputStream = connection.getInputStream();
      Map<String, List<String>> headerFields = connection.getHeaderFields();
      IGetAssetDetailsStrategyModel assetContentsModel = new GetAssetDetailsStrategyModel();
      assetContentsModel.setResponseCode(responseCode);
      assetContentsModel.setInputStream(inputStream);
      assetContentsModel.setResponseHeaders(getResponseHeaders(headerFields));
      return assetContentsModel;
    }
    else if (responseCode == 401) {
      IGetAssetDetailsStrategyModel assetContentsModel = new GetAssetDetailsStrategyModel();
      assetContentsModel.setResponseCode(responseCode);
      return assetContentsModel;
    }
    else if (responseCode == 404) {
      throw new AssetObjectNotFoundException();
    }
    else {
      PluginException exception = new PluginException();
      exception.getDevExceptionDetails()
          .get(0)
          .setDetailMessage("Get Asset Failed with Response Code: " + responseCode + ", Message: "
              + connection.getResponseMessage());
      throw exception;
    }
  }
  
  private void checkAssetUploadStatus(IGetAssetDetailsRequestModel model, String assetKey)
      throws AssetUploadInProgressException, Exception
  {
    if (assetUploadStatusMap.containsKey(assetKey)) {
      String progress = assetUploadStatusMap.get(assetKey);
      if (progress.equals("0") || progress.equals("50")) {
        Map<String, String> properties = new HashMap<>();
        properties.put("progress", progress);
        AssetUploadInProgressException e = new AssetUploadInProgressException();
        ExceptionUtil.setFailureDetailsToFailureObject(new ExceptionModel(), e, null, null,
            properties);
        throw e;
      }
      else if (progress.equals("-1")) {
        assetUploadStatusMap.remove(assetKey);
        throw new Exception(
            "Asset Upload Failed. Container = " + model.getContainer() + ", Key = " + assetKey);
      }
    }
    else {
      // Check if asset is uploaded in server
      IAssetUploadStatusCheckModel statusRequestModel = new AssetUploadStatusCheckModel(
          model.getContainer(), assetKey);
      IAssetUploadStatusModel statusResponse = null; /*
                                                     getAssetUploadStatusStrategy
                                                     .execute(statusRequestModel);*/
      // FIXME: temporary fix to make compatible with previous data
      if (statusResponse != null) {
        if (statusResponse == null || statusResponse.getStatus()
            .equalsIgnoreCase("Failed")) {
          throw new Exception("Asset Upload Failed. Container = "
              + statusRequestModel.getContainer() + ", Key = " + statusRequestModel.getKey());
        }
        else if (statusResponse.getStatus()
            .equalsIgnoreCase("InProgress")) {
          Map<String, String> properties = new HashMap<>();
          properties.put("progress", statusResponse.getProgress());
          AssetUploadInProgressException e = new AssetUploadInProgressException();
          ExceptionUtil.setFailureDetailsToFailureObject(new ExceptionModel(), e, null, null,
              properties);
          throw e;
        }
        model.setAssetKey(statusResponse.getId());
      }
    }
  }
  
  private Map<String, String> getResponseHeaders(Map<String, List<String>> headerFields)
  {
    Map<String, String> responseHeaders = new HashMap<>();
    
    List<String> acceptRanges = headerFields.get(CommonConstants.HEADER_ACCEPT_RANGES);
    if (acceptRanges != null) {
      responseHeaders.put(CommonConstants.HEADER_ACCEPT_RANGES, acceptRanges.get(0));
    }
    List<String> contentLength = headerFields.get(CommonConstants.HEADER_CONTENT_LENGTH);
    if (contentLength != null) {
      responseHeaders.put(CommonConstants.HEADER_CONTENT_LENGTH, contentLength.get(0));
    }
    List<String> contentType = headerFields.get(CommonConstants.HEADER_CONTENT_TYPE);
    if (contentType != null) {
      responseHeaders.put(CommonConstants.HEADER_CONTENT_TYPE, contentType.get(0));
    }
    List<String> eTag = headerFields.get(CommonConstants.HEADER_ETAG);
    if (eTag != null) {
      responseHeaders.put(CommonConstants.HEADER_ETAG, eTag.get(0));
    }
    // responseHeaders.put(CommonConstants.HEADER_CONTENT_LENGTH,
    // headerFields.get(CommonConstants.HEADER_CONTENT_LENGTH).get(0));
    // responseHeaders.put(CommonConstants.HEADER_CONTENT_TYPE,
    // headerFields.get(CommonConstants.HEADER_CONTENT_TYPE).get(0));
    // responseHeaders.put(CommonConstants.HEADER_ETAG,
    // headerFields.get(CommonConstants.HEADER_ETAG).get(0));
    List<String> contentRangeList = headerFields.get(CommonConstants.HEADER_CONTENT_RANGE);
    if (contentRangeList != null && !contentRangeList.isEmpty()) {
      String contentRange = contentRangeList.get(0);
      if (contentRange != null) {
        responseHeaders.put(CommonConstants.HEADER_CONTENT_RANGE, contentRange);
      }
    }
    List<String> fileName = headerFields.get(CommonConstants.HEADER_FILENAME);
    if(fileName != null) {
      responseHeaders.put(CommonConstants.HEADER_FILENAME, fileName.get(0));
    }
    return responseHeaders;
  }
}
