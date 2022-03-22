package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IAuthenticateUserStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.DataOutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component("authenticateAssetStrategy")
public class AuthenticateAssetStrategy implements IAuthenticateUserStrategy {
  
  @Resource(name = "assetStoreAuthenticationMap")
  protected Map<String, String> assetStoreAuthenticationMap;
  
  @Autowired
  protected String              assetStoreURL;
  
  @Resource(name = "cookies")
  protected List<String>        cookies;
  
  @Override
  public IUserModel execute(IUserModel model) throws Exception
  {
    try {
      // RDBMSLogger.instance().info("Authenticating Asset Server......");
      URL uri = new URL(assetStoreURL + "authenticate/");
      HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
      connection.setRequestMethod("POST");
      ObjectMapper jsonMapper = new ObjectMapper();
      String authenticationJSON = jsonMapper.writeValueAsString(assetStoreAuthenticationMap);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.connect();
      
      DataOutputStream output = new DataOutputStream(connection.getOutputStream());
      output.writeBytes(authenticationJSON);
      output.flush();
      output.close();
      
      int responseCode = connection.getResponseCode();
      
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // RDBMSLogger.instance().info("Asset Server : SUCCESS!");
        String setCookie = connection.getHeaderField("Set-Cookie");
        List<HttpCookie> httpCookies = HttpCookie.parse(setCookie);
        cookies.removeAll(cookies);
        for (HttpCookie httpCookie : httpCookies) {
          cookies.add(httpCookie.toString());
        }
        /*RDBMSLogger.instance().info("Content-------start-----");
        DataInputStream input = new DataInputStream(connection.getInputStream());
        BufferedReader d = new BufferedReader(new InputStreamReader(input));
        RDBMSLogger.instance().info(d.readLine());
        String inputLine;
        while ((inputLine = d.readLine()) != null) {
            RDBMSLogger.instance().info(inputLine);
        }
        RDBMSLogger.instance().info("Content-------end-----");*/
      }
      else {
        throw new Exception();
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Asset Server : Failure!");
      throw new Exception("Asset server authentication failed", e);
    }
    return null;
  }
}
