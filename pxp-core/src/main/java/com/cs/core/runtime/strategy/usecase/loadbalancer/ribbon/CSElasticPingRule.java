package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * CSElasticPingRule implements IPing interface of com.netflix.loadbalancer.
 * Checks server's health.
 */
public class CSElasticPingRule implements IPing {
  
  String  partialURL = "/ping";
  boolean isSecure   = false;
  
  public CSElasticPingRule()
  {
  }
  
  public CSElasticPingRule(boolean isSecure, String pingAppendString)
  {
    this.isSecure = isSecure;
    this.partialURL = (pingAppendString != null) ? pingAppendString : "";
  }
  
  private String getPingAppendString()
  {
    return partialURL;
  }
  
  public boolean isAlive(Server server)
  {
    String urlStr = "";
    if (isSecure) {
      urlStr = "https://";
    }
    else {
      urlStr = "http://";
    }
    urlStr += server.getId();
    urlStr += getPingAppendString();
    
    boolean isAlive = false;
    
    String content = null;
    HttpURLConnection connection;
    try {
      connection = makeConnection(urlStr);
      connection.connect();
      
      content = getResponseString(connection.getInputStream());
      /*JsonParser parse = new JsonParser();
      			JsonObject obj = (JsonObject) parse.parse(content);
      */
      content = content.replace("\"", "");
      isAlive = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
      if (content.equalsIgnoreCase("pong")) {
        isAlive = true;
      }
      else {
        isAlive = false;
      }
      connection.disconnect();
      
    }
    catch (IOException e) {
      if (e.getMessage()
          .contains("Connection refused")) {
      }
      else {
        RDBMSLogger.instance().exception(e);
      }
    }
    
    return isAlive;
  }
  
  private HttpURLConnection makeConnection(String urlStr) throws IOException
  {
    URL url = new URL(urlStr);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    
    return connection;
  }
  
  private String getResponseString(InputStream in) throws IOException
  {
    StringBuilder content = new StringBuilder();
    DataInputStream input = new DataInputStream(in);
    BufferedReader d = new BufferedReader(new InputStreamReader(input, "UTF-8"));
    String str;
    while ((str = d.readLine()) != null) {
      content.append(str);
    }
    
    return content.toString();
  }
}
