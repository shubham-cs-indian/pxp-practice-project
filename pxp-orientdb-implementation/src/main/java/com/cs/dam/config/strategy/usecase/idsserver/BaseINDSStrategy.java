package com.cs.dam.config.strategy.usecase.idsserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.core.runtime.interactor.constants.INDSConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BaseINDSStrategy {
  
  /**
   * This method is pinging load balancer and getting it's configured indesign
   * server's info
   * 
   * @param requestData
   * @param taskRequestType
   * @param loadBalancerProperties
   * @return
   * @throws IOException
   */
  public String sendRequest(String requestData, String taskRequestType,
      IInDesignServerInstance loadBalancerProperties) throws IOException
  {
    String response = null;
    String hostname = loadBalancerProperties.getHostName();
    String port = loadBalancerProperties.getPort();
    URL url = new URL("http://" + hostname + ":" + port);
    
    try {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty(INDSConstants.TASK_REQUEST_TYPE, taskRequestType);
      connection.connect();
      
      DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOutputStream, "UTF-8"));
      writer.write(requestData);
      writer.close();
      
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        response = getResponseString(connection.getInputStream());
      }
      else {
        throw new Exception();
      }
    }
    catch (Exception exception) {
      response = getResponseStringInCaseException(taskRequestType);
    }
    
    return response;
  }
  
  /**
   * This method is pinging load balancer and getting it's configured indesign
   * server's info as inputstreams
   * @param requestData
   * @param taskRequestType
   * @param loadBalancerProperties
   * @return
   * @throws IOException
   */
  public InputStream sendRequestForInputStream(String requestData, String taskRequestType,
      IInDesignServerInstance loadBalancerProperties) throws IOException
  {
    
    String hostname = loadBalancerProperties.getHostName();
    String port = loadBalancerProperties.getPort();
    
    URL url = new URL("http://" + hostname + ":" + port);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Encoding-Type", "UTF-8");
    connection.setRequestProperty(INDSConstants.TASK_REQUEST_TYPE, taskRequestType);
    connection.connect();
    
    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
    BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(dataOutputStream, System.getProperty("file.encoding", "UTF-8")));
    writer.write(requestData);
    writer.close();
    
    return connection.getInputStream();
  }
  
  /**
   * Convert inputstream to string
   * @param inputStream
   * @return
   * @throws IOException
   */
  private String getResponseString(InputStream inputStream) throws IOException
  {
    StringBuilder content = new StringBuilder();
    DataInputStream dataInputStream = new DataInputStream(inputStream);
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(dataInputStream, "UTF-8"));
    String line;
    
    while ((line = bufferedReader.readLine()) != null) {
      content.append(line);
    }
    
    return content.toString();
  }
  
  /**
   * This method is sending dummy response if there is any exception occur while
   * execution.
   * 
   * @return response string
   * @throws JsonProcessingException
   */
  private String getResponseStringInCaseException(String taskRequestType)
      throws JsonProcessingException
  {
    Map<String, Object> returnObject = new HashMap<String, Object>();
    returnObject.put(IINDSPingTaskResponseModel.INDS_LOAD_BALANCER, null);
    returnObject.put(IINDSPingTaskResponseModel.PINGED_SERVERS, new ArrayList<>());
    returnObject.put(IINDSPingTaskResponseModel.IS_LOAD_BALANCER_CONNECTED, false);
    
    return ObjectMapperUtil.writeValueAsString(returnObject);
  }
}
