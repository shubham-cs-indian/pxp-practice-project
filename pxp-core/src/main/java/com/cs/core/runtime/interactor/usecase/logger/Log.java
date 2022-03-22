package com.cs.core.runtime.interactor.usecase.logger;

import com.cs.core.runtime.interactor.model.logger.IJobData;
import com.cs.core.runtime.interactor.model.logger.ILogData;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.JobData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.entity.NStringEntity;

public class Log implements Runnable {
  
  private ILogData       logData;
  
  private String         url;
  
  protected ObjectMapper mapper = new ObjectMapper();
  
  public Log(ILogData logData, String url)
  {
    this.logData = logData;
    this.url = url;
  }
  
  @Override
  public void run()
  {
    /*	try {
    	CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
    	client.start();
    	String id = this.logData.getId();
    	URL URI = new URL(url + id);
    	String uri = URI.toString();
    	//check if document already exists in logs
    	HttpGet getRequest = new HttpGet(uri);
    	Future<HttpResponse> future = client.execute(getRequest, null);
    	HttpResponse response = future.get();
    	int statusCode = response.getStatusLine().getStatusCode();
    	//if document found update the document
    	if (statusCode == HttpURLConnection.HTTP_OK) {
    		updateOldLogEntry(client, id, response);
    	}
    	else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
    		createNewLogEntry(client, id, uri);
    	}
    	client.close();
    }
    catch (Exception e) {
    	RDBMSLogger.instance().exception(e);
    }*/
  }
  
  @SuppressWarnings("unchecked")
  private void updateOldLogEntry(CloseableHttpAsyncClient client, String id, HttpResponse response)
      throws IOException, JsonParseException, JsonMappingException, JsonProcessingException,
      MalformedURLException, InterruptedException, ExecutionException, IllegalAccessException,
      InstantiationException
  {
    URL URI;
    String uri;
    Future<HttpResponse> future;
    int statusCode;
    String responseString = IOUtils.toString(response.getEntity()
        .getContent(), "UTF-8");
    Map<String, Object> responseMap = mapper.readValue(responseString,
        new TypeReference<Map<String, Object>>()
        {
        });
    Map<String, Object> responseSource = (Map<String, Object>) responseMap.get("_source");
    String type = (String) responseSource.get("type");
    ILogData mergedData = null;
    ILogData oldLogData = null;
    List<String> subJobIds = new ArrayList<>();
    List<String> successSubJobIds = new ArrayList<>();
    // depeding upon type update document
    if (type.equals("transaction")) {
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      oldLogData = mapper.readValue(mapper.writeValueAsString(responseSource),
          TransactionData.class);
    }
    else if (type.equals("job")) {
      oldLogData = mapper.readValue(mapper.writeValueAsString(responseSource), JobData.class);
      String parentTransactionId = ((IJobData) oldLogData).getParentTransactionId();
      if (parentTransactionId != null) {
        URI = new URL(url + parentTransactionId);
        uri = URI.toString();
        HttpGet getRequestForParentDocument = new HttpGet(uri);
        future = client.execute(getRequestForParentDocument, null);
        response = future.get();
        statusCode = response.getStatusLine()
            .getStatusCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
          responseString = IOUtils.toString(response.getEntity()
              .getContent(), "UTF-8");
          responseMap = mapper.readValue(responseString, new TypeReference<Map<String, Object>>()
          {
          });
          responseSource = (Map<String, Object>) responseMap.get("_source");
          TransactionData parentLogData = mapper
              .readValue(mapper.writeValueAsString(responseSource), TransactionData.class);
          if (logData.getExecutionStatus()
              .equals("failure")
              && (parentLogData.getExecutionStatus()
                  .equals("pending")
                  || parentLogData.getExecutionStatus()
                      .equals("success"))) {
            parentLogData.setExecutionStatus("failure");
          }
          else if (logData.getExecutionStatus()
              .equals("success")) {
            parentLogData.getSuccessSubJobIds()
                .add(id);
            if (!parentLogData.getExecutionStatus()
                .equals("failure")) {
              if (!parentLogData.getSubJobIds()
                  .equals(parentLogData.getSuccessSubJobIds())) {
                // parentLogData.setExecutionStatus("pending");
              }
              else {
                parentLogData.setExecutionStatus("success");
              }
            }
          }
          HttpPost postRequestForParentDocument = new HttpPost(uri);
          String parentData = mapper.writeValueAsString(parentLogData);
          NStringEntity entity = new NStringEntity(parentData, ContentType.APPLICATION_JSON);
          postRequestForParentDocument.setEntity(entity);
          future = client.execute(postRequestForParentDocument, null);
          future.get();
        }
      }
    }
    URI = new URL(url + id);
    uri = URI.toString();
    mergedData = mergeObjects(logData, oldLogData);
    if (type.equals("transaction")) {
      subJobIds.addAll(((ITransactionData) logData).getSubJobIds());
      successSubJobIds.addAll(((ITransactionData) logData).getSuccessSubJobIds());
      ((ITransactionData) mergedData).setSubJobIds(subJobIds);
      ((ITransactionData) mergedData).setSuccessSubJobIds(successSubJobIds);
    }
    HttpPost postRequest = new HttpPost(uri);
    String data = mapper.writeValueAsString(mergedData);
    NStringEntity entity = new NStringEntity(data, ContentType.APPLICATION_JSON);
    postRequest.setEntity(entity);
    future = client.execute(postRequest, null);
    future.get();
  }
  
  @SuppressWarnings("unchecked")
  private void createNewLogEntry(CloseableHttpAsyncClient client, String id, String uri)
      throws JsonProcessingException, InterruptedException, ExecutionException,
      MalformedURLException, IOException, JsonParseException, JsonMappingException
  {
    URL URI;
    Future<HttpResponse> future;
    HttpResponse response;
    int statusCode;
    HttpPut putRequest = new HttpPut(uri);
    String data = mapper.writeValueAsString(logData);
    NStringEntity entity = new NStringEntity(data, ContentType.APPLICATION_JSON);
    putRequest.setEntity(entity);
    future = client.execute(putRequest, null);
    future.get();
    if (logData.getType()
        .equals("job")) {
      String parentTransactionId = ((IJobData) logData).getParentTransactionId();
      if (parentTransactionId != null) {
        URI = new URL(url + parentTransactionId);
        uri = URI.toString();
        HttpGet getParentTransactionDocumentRequest = new HttpGet(uri);
        future = client.execute(getParentTransactionDocumentRequest, null);
        response = future.get();
        statusCode = response.getStatusLine()
            .getStatusCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
          String responseString = IOUtils.toString(response.getEntity()
              .getContent(), "UTF-8");
          Map<String, Object> responseMap = mapper.readValue(responseString,
              new TypeReference<Map<String, Object>>()
              {
              });
          Map<String, Object> responseSource = (Map<String, Object>) responseMap.get("_source");
          ITransactionData parentTransaction = mapper
              .readValue(mapper.writeValueAsString(responseSource), TransactionData.class);
          if (!parentTransaction.getSubJobIds()
              .contains(id))
            parentTransaction.getSubJobIds()
                .add(id);
          if (!parentTransaction.getExecutionStatus()
              .equals("failure")) {
            
            if (logData.getExecutionStatus()
                .trim()
                .toLowerCase()
                .equals("failure"))
              parentTransaction.setExecutionStatus("failure");
          }
          if (parentTransaction.getExecutionStatus()
              .equals("pending")
              && logData.getExecutionStatus()
                  .trim()
                  .toLowerCase()
                  .equals("success"))
            parentTransaction.setExecutionStatus("success");
          if (logData.getExecutionStatus()
              .trim()
              .toLowerCase()
              .equals("success")
              && !parentTransaction.getSuccessSubJobIds()
                  .contains(id))
            parentTransaction.getSuccessSubJobIds()
                .add(id);
          HttpPost postRequestToUpdateParentTransaction = new HttpPost(uri);
          String parentTransactionString = mapper.writeValueAsString(parentTransaction);
          entity = new NStringEntity(parentTransactionString, ContentType.APPLICATION_JSON);
          postRequestToUpdateParentTransaction.setEntity(entity);
          future = client.execute(postRequestToUpdateParentTransaction, null);
          future.get();
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T mergeObjects(T first, T second)
      throws IllegalAccessException, InstantiationException
  {
    Class<?> clazz = first.getClass();
    Field[] fields = clazz.getDeclaredFields();
    Object returnValue = clazz.newInstance();
    for (Field field : fields) {
      field.setAccessible(true);
      Object value1 = field.get(first);
      Object value2 = field.get(second);
      Object value = null;
      if (value1 instanceof List<?> && value2 instanceof List<?>) {
        ((List<String>) value1).addAll((List<String>) value2);
        value = value1;
      }
      else {
        value = (value1 != null) ? value1 : value2;
      }
      field.set(returnValue, value);
    }
    return (T) returnValue;
  }
}
