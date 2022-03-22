package com.cs.core.config.strategy.usecase.swift;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.AssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.assetserver.AssetServerAuthenticationFailedException;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticateAssetServerStrategy implements IAuthenticateAssetServerStrategy {
  
  public static final String REQUEST_HEADER_AUTH_USER    = "X-Auth-User";
  public static final String REQUEST_HEADER_AUTH_KEY     = "X-Auth-Key";
  public static final String RESPONSE_HEADER_AUTH_TOKEN  = "X-Auth-Token";
  public static final String RESPONSE_HEADER_STORAGE_URL = "X-Storage-Url";
  public static final int    RESPONSE_CODE_200           = 200;
  public static final int    RESPONSE_CODE_401           = 401;
  
  @Value("${system.mode}")
  protected String           mode;
  @Autowired
  String                     assetServerAuthURL;
  @Resource(name = "assetStoreAuthenticationMap")
  Map<String, String>        assetStoreAuthenticationMap;
  
  @Override
  public IAssetServerDetailsModel execute(IModel model) throws Exception
  {
    URL uri = new URL(assetServerAuthURL);
   
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
    connection.setRequestProperty(REQUEST_HEADER_AUTH_USER,
        assetStoreAuthenticationMap.get("account") + ":" + assetStoreAuthenticationMap.get("user"));
    connection.setRequestProperty(REQUEST_HEADER_AUTH_KEY,
        assetStoreAuthenticationMap.get("passkey"));
    connection.connect();
    
    int responseCode = connection.getResponseCode();
    
    if (responseCode == RESPONSE_CODE_200) {
      Map<String, List<String>> responseHeaders = connection.getHeaderFields();
      IAssetServerDetailsModel assetServerDetails = new AssetServerDetailsModel(
          responseHeaders.get(RESPONSE_HEADER_STORAGE_URL)
              .get(0),
          responseHeaders.get(RESPONSE_HEADER_AUTH_TOKEN)
              .get(0));
      return assetServerDetails;
    }
    else {
      AssetServerAuthenticationFailedException exception = new AssetServerAuthenticationFailedException();
      exception.getDevExceptionDetails()
          .get(0)
          .setDetailMessage("Authentication Failed with Response Code: " + responseCode
              + ", Message: " + connection.getResponseMessage());
      throw exception;
    }
  }
}
