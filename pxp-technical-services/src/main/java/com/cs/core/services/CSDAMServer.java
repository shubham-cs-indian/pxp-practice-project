package com.cs.core.services;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.exception.PluginException;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.assetserver.AssetObjectNotFoundException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;

@SuppressWarnings("unchecked")
public final class CSDAMServer {

  private static final String CONTAINER                   = "container";
  private static final String ASSET_KEY                   = "assetKey";
  private static final String AUTH_TOKEN                  = "authToken";
  private static final String STORAGE_URL                 = "storageUrl";
  public static final String  REQUEST_HEADER_AUTH_TOKEN   = "X-Auth-Token";
  private static final String REQUEST_HEADER_AUTH_USER    = "X-Auth-User";
  private static final String REQUEST_HEADER_AUTH_KEY     = "X-Auth-Key";
  public static final String  RESPONSE_HEADER_AUTH_TOKEN  = "X-Auth-Token";
  public static final String  RESPONSE_HEADER_STORAGE_URL = "X-Storage-Url";
  
  public static final String  HEADER_IF_MATCH             = "If-Match";
  public static final String  HEADER_RANGE                = "Range";
  public static final String  HEADER_ACCEPT_RANGES        = "Accept-Ranges";
  public static final String  HEADER_CONTENT_LENGTH       = "Content-Length";
  public static final String  HEADER_CONTENT_RANGE        = "Content-Range";
  public static final String  HEADER_CONTENT_TYPE         = "Content-Type";
  public static final String  HEADER_ETAG                 = "Etag";
  
  public static final String  HEADER_FILENAME             = "X-Object-Meta-Name";
  
  private final IJSONContent serverDetails = new JSONContent();
  // Singleton implementation
  private static CSDAMServer Instance;

  private CSDAMServer() throws CSInitializationException, PluginException {
    connect();
  }

  public static CSDAMServer instance() throws CSInitializationException, PluginException {
    if (Instance == null) {
      Instance = new CSDAMServer();
    }
    return Instance;
  }

  /**
   * Authenticate and (re)connect to DAM server
   *
   * @throws CSInitializationException
   * @throws PluginException 
   */
  public void connect() throws CSInitializationException, PluginException
  {
    String damURL = null;
    URL uri = null;
    String user = null;
    String account = null;
    String passkey = null;
    
    try {
      damURL = CSProperties.instance()
          .getString("asset.connectionString");
      user = CSProperties.instance()
          .getString("asset.user");
      account = CSProperties.instance()
          .getString("asset.account");
      passkey = CSProperties.instance()
          .getString("asset.passkey");
    }
    catch(CSInitializationException ex) {
      throw new CSInitializationException(ex.getMessage());
    }
    
    try {
      uri = new URL(damURL + '/' + "auth/v1.0/");
      HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
      connection.setRequestMethod("GET");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty(REQUEST_HEADER_AUTH_USER, account + ":" + user);
      connection.setRequestProperty(REQUEST_HEADER_AUTH_KEY, passkey);
      connection.connect();
      
      int responseCode = connection.getResponseCode();
      if (responseCode == 200 || responseCode == 206) {
        Map<String, List<String>> responseHeaders = connection.getHeaderFields();
        serverDetails.setField(RESPONSE_HEADER_STORAGE_URL,
            responseHeaders.get(RESPONSE_HEADER_STORAGE_URL)
                .get(0));
        serverDetails.setField(RESPONSE_HEADER_AUTH_TOKEN,
            responseHeaders.get(RESPONSE_HEADER_AUTH_TOKEN)
                .get(0));
      }
      else if (responseCode == 404) {
        throw new AssetObjectNotFoundException();
      }
      else {
        PluginException exception = new PluginException();
        exception.getDevExceptionDetails().get(0)
            .setDetailMessage("Connection with swift server failed with Response Code: "
                + responseCode + ", Message: " + connection.getResponseMessage());
        throw exception;
      }
    }
    catch (IOException ex) {
      RDBMSLogger.instance().exception(ex);
      
      PluginException exception = new PluginException();
      exception.getDevExceptionDetails()
          .get(0).setDetailMessage("Connection with swift server failed.");
      
      throw exception;
    }
  }

  /**
   * Upload asset
   *
   * @param uploadModel
   * @return the HTTP code of the transaction
   * @throws IOException
   */
  private int uploadAssetToServer(Map<String, Object> uploadModel) throws IOException {
    String storageUrl = (String) uploadModel.get(STORAGE_URL);
    String container = (String) uploadModel.get(CONTAINER);
    String assetKey = (String) uploadModel.get("assetObjectKey");
    Map<String, String> assetDataMap = (Map<String, String>) uploadModel.get("assetData");
    // byte array is sent if it is thumbUpload
    byte[] assetBytes = (byte[]) uploadModel.get("assetBytes");

    if (assetBytes == null || assetBytes.length == 0) {
      InputStream iStream = new FileInputStream(assetDataMap.get("X-Object-Meta-Name"));
      assetBytes = IOUtils.toByteArray(iStream);
    }

    URL uri = new URL(storageUrl + "/" + container + "/" + assetKey);
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

      try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())) {
        dataOutputStream.write(assetBytes);
      }
      return connection.getResponseCode();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
  
  /**
   * Secure way to upload an asset
   *
   * @param uploadModel
   * @throws CSInitializationException
   * @throws PluginException 
   */
  public void uploadAsset(Map<String, Object> uploadModel) throws CSInitializationException, PluginException {
    try {
      Integer responseCode = uploadAssetToServer(uploadModel);
      if (responseCode == 401) {
        connect();
        Map<String, Object> assetDataMap = (Map<String, Object>) uploadModel.get("assetData");
        assetDataMap.put(RESPONSE_HEADER_AUTH_TOKEN, serverDetails.getInitField(RESPONSE_HEADER_AUTH_TOKEN, ""));
        uploadAssetToServer(uploadModel);
      }
    } catch (IOException ex) {
      throw new CSInitializationException("upload asset failed", ex);
    }
  }
  
  public void uploadAssetToSharedContainer(Map<String, Object> uploadModel) throws CSInitializationException, PluginException {
    try {
      Integer responseCode = uploadAssetToServer(uploadModel);
      if (responseCode == 401) {
        connect();
        Map<String, Object> assetServerDetails = (Map<String, Object>) uploadModel.get("assetData");
        assetServerDetails.put(AUTH_TOKEN, serverDetails.getInitField(RESPONSE_HEADER_AUTH_TOKEN, ""));
        uploadAssetToServer(uploadModel);
      }
    } catch (IOException ex) {
      throw new CSInitializationException("upload asset failed", ex);
    }
  }
  
  public Map<String, Object> getAsset(Map<String, Object> getModel) throws CSInitializationException, IOException{
    
    try {
      Map<String, Object> responseModel = getAssetFromServer(getModel);
      Integer responseCode = (Integer) responseModel.get("responseCode");
      if (responseCode == 401) {
        connect();
        Map<String, Object> assetServerDetails = (Map<String, Object>) getModel.get("assetServerDetails");
        assetServerDetails.put(STORAGE_URL, serverDetails.getInitField(RESPONSE_HEADER_STORAGE_URL, ""));
        assetServerDetails.put(AUTH_TOKEN, serverDetails.getInitField(RESPONSE_HEADER_STORAGE_URL, ""));
        return getAssetFromServer(getModel);
      }
      return responseModel;
    }
    catch (PluginException | CSInitializationException e) {
      throw new CSInitializationException("Get asset from server failed", e);
    }
  }
  
  private Map<String, Object> getAssetFromServer(Map<String, Object> getModel) throws IOException, PluginException{
    String assetKey = (String) getModel.get(ASSET_KEY);
    // get asset from swift
    Map<String, Object> assetServerDetails = (Map<String, Object>) getModel.get("assetServerDetails");
    URL uri = new URL(assetServerDetails.get(STORAGE_URL) + "/" + getModel.get(CONTAINER) + "/" + assetKey);
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("GET");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty(REQUEST_HEADER_AUTH_TOKEN, (String) assetServerDetails.get(AUTH_TOKEN));
    Map<String, String> requestHeaders = (Map<String, String>) getModel.get("requestHeaders");
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
      Map<String, Object> assetContentsModel = new HashMap<>();
      assetContentsModel.put("responseCode", responseCode);
      assetContentsModel.put("inputStream", inputStream);
      assetContentsModel.put("responseHeaders", getResponseHeaders(headerFields));
      return assetContentsModel;
    }
    else if (responseCode == 401) {
      Map<String, Object> assetContentsModel = new HashMap<>();
      assetContentsModel.put("responseCode", responseCode);
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
  
  private Map<String, String> getResponseHeaders(Map<String, List<String>> headerFields)
  {
    Map<String, String> responseHeaders = new HashMap<>();
    
    List<String> acceptRanges = headerFields.get(HEADER_ACCEPT_RANGES);
    if (acceptRanges != null) {
      responseHeaders.put(HEADER_ACCEPT_RANGES, acceptRanges.get(0));
    }
    List<String> contentLength = headerFields.get(HEADER_CONTENT_LENGTH);
    if (contentLength != null) {
      responseHeaders.put(HEADER_CONTENT_LENGTH, contentLength.get(0));
    }
    List<String> contentType = headerFields.get(HEADER_CONTENT_TYPE);
    if (contentType != null) {
      responseHeaders.put(HEADER_CONTENT_TYPE, contentType.get(0));
    }
    List<String> eTag = headerFields.get(HEADER_ETAG);
    if (eTag != null) {
      responseHeaders.put(HEADER_ETAG, eTag.get(0));
    }
    // responseHeaders.put(CommonConstants.HEADER_CONTENT_LENGTH,
    // headerFields.get(CommonConstants.HEADER_CONTENT_LENGTH).get(0));
    // responseHeaders.put(CommonConstants.HEADER_CONTENT_TYPE,
    // headerFields.get(CommonConstants.HEADER_CONTENT_TYPE).get(0));
    // responseHeaders.put(CommonConstants.HEADER_ETAG,
    // headerFields.get(CommonConstants.HEADER_ETAG).get(0));
    List<String> contentRangeList = headerFields.get(HEADER_CONTENT_RANGE);
    if (contentRangeList != null && !contentRangeList.isEmpty()) {
      String contentRange = contentRangeList.get(0);
      if (contentRange != null) {
        responseHeaders.put(HEADER_CONTENT_RANGE, contentRange);
      }
    }
    
    //get the file name
    List<String> fileName = headerFields.get(HEADER_FILENAME);
    if(fileName != null) {
      responseHeaders.put(HEADER_FILENAME, fileName.get(0));
    }
    
    return responseHeaders;
  }

  /**
   * @return the server details obtained from last authentication
   */
  public IJSONContent getServerInformation() {
    return serverDetails;
  }

  public int deleteAssetFromSwiftServer(Map<String, Object> requestMap) throws IOException, PluginException, CSInitializationException
  {
    Map<String, String> assetServerDetails = (Map<String, String>) requestMap.get("assetServerDetails");
    URL uri = new URL(assetServerDetails.get(STORAGE_URL) + "/" + requestMap.get(IGetAssetDetailsRequestModel.CONTAINER) + "/"
        + requestMap.get(IGetAssetDetailsRequestModel.ASSET_KEY));
    
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("DELETE");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty(REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.get(AUTH_TOKEN));
    Map<String, String> requestHeaders = (Map<String, String>) requestMap.get(IGetAssetDetailsRequestModel.REQUEST_HEADERS);
    if (requestHeaders != null) {
      for (String headerName : requestHeaders.keySet()) {
        connection.setRequestProperty(headerName, requestHeaders.get(headerName));
      }
    }
    
    connection.connect();
    return connection.getResponseCode();
  }
  
  /**
   * Returns the header information from Swift server for the passed assetKey.
   * Returns empty HashMap if response code is 401.
   * 
   * @param requestMap
   * @return
   * @throws IOException
   * @throws PluginException
   */
  private Map<String, List<String>> getHeaderInformation(Map<String, Object> requestMap)
      throws IOException, PluginException
  {
    String assetKey = (String) requestMap.get(ASSET_KEY);
    URL uri = new URL(requestMap.get(STORAGE_URL) + "/" + requestMap.get(CONTAINER) + "/" + assetKey);
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("HEAD");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty(REQUEST_HEADER_AUTH_TOKEN, (String) requestMap.get(AUTH_TOKEN));
    connection.connect();
    int responseCode = connection.getResponseCode();
    
    if (responseCode == 200 || responseCode == 206) {
      return connection.getHeaderFields();
    }
    else if (responseCode == 401) {
      return new HashMap<>();
    }
    else if (responseCode == 404) {
      throw new AssetObjectNotFoundException();
    }
    else {
      PluginException exception = new PluginException();
      exception.getDevExceptionDetails().get(0).setDetailMessage("Get Asset Failed with Response Code: "
      + responseCode + ", Message: "+ connection.getResponseMessage());
      throw exception;
    }
  }
  
  /***
   * Returns the header information from Swift server for the passed assetKey.
   * This method tries to reconnect once if on first attempt Response Code is 401 (Unauthorized).
   * 
   * @param requestMap
   * @return
   * @throws IOException
   * @throws PluginException
   * @throws CSInitializationException
   */
  public Map<String, List<String>> getHeaderInformationForAsset(Map<String, Object> requestMap)
      throws IOException, PluginException, CSInitializationException
  {
    try {
      Map<String, List<String>> responseModel = getHeaderInformation(requestMap);
      if (responseModel.isEmpty()) {
        connect();
        requestMap.put(STORAGE_URL, serverDetails.getInitField(RESPONSE_HEADER_STORAGE_URL, ""));
        requestMap.put(AUTH_TOKEN, serverDetails.getInitField(RESPONSE_HEADER_STORAGE_URL, ""));
        return getHeaderInformation(requestMap);
      }
      return responseModel;
    }
    catch (PluginException | CSInitializationException e) {
      throw new CSInitializationException("Get asset from server failed :: " + e.getMessage(), e);
    }
  }
}