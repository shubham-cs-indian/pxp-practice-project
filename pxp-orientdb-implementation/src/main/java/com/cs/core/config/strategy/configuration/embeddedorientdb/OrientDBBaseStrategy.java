package com.cs.core.config.strategy.configuration.embeddedorientdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.cs.constants.CommonConstants;
import com.cs.core.config.strategy.constants.ConfigRequestMappingConstants;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.InteractorData;
import com.cs.core.runtime.interactor.model.logger.JobData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.ExceptionUtil;
import com.cs.core.runtime.strategy.model.loadbalancer.CSLoadBalancingModel;
import com.cs.core.runtime.strategy.usecase.loadbalancer.ICSLoadBalancer;
import com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon.CSHttpResponse;
import com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon.OrientDBLoadBalancer;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.PXPLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonParser;
import com.netflix.loadbalancer.BaseLoadBalancer;

public class OrientDBBaseStrategy extends ConfigRequestMappingConstants {

  @Autowired
  protected String                orientDBHost;

  @Autowired
  protected String                orientDBPort;

  @Autowired
  protected String                orientDBdatabase;

  @Value("${system.mode}")
  protected String                mode;
  
  @Autowired
  protected Authenticator         getAuthenticator;
  //
  protected String                orientDBjURI;

  protected String                orientDBPluginUrlEnd = "";

  protected String                USER_AGENT           = "Mozilla/5.0";

  @Autowired
  protected TransactionThreadData transactionThread;
  /*
  @Autowired
  protected Logger                logger;*/

  @Autowired
  protected String                orientLoadBalancerLog;

  @Autowired
  protected boolean               executeWithLoadBalancer;

  @Autowired
  protected ApplicationContext    applicationContext;

  @Value("#{new Boolean('pxpconfiguration.isTimeLoggingEnabled')}")
  protected Boolean               isTimeLoggingEnabled;
  
  @Autowired
  protected PXPLogger             pxpLogger;
  
  
  /*  public String executePlugin(String useCase, Object requestMap) throws Exception
  {
    JobData jobData = new JobData();
    jobData.setKlassName(this.getClass()
        .getSimpleName());
    jobData.setUseCase(useCase);

    URL URI = new URL("http://" + orientDBHost + ":" + orientDBPort + "/" + useCase + "/"
        + orientDBdatabase + "/");
    jobData.setRequestURL(URI.toString());
    jobData.setRequestMethod("POST");
    if (mode.equalsIgnoreCase("Development")) {
      System.out.println("======================= SERVER PLUGIN REQUEST======================");
      System.out.println("\nURI : " + URI);
      // System.out.println("\nParam : " + contentJson);
    }
    jobData.setStartTime(System.currentTimeMillis());
    Authenticator.setDefault(getAuthenticator);
    HttpURLConnection connection = prepareConnection(URI, "POST");
    String contentJson = ObjectMapperUtil.writeValueAsString(requestMap);
    if (contentJson != null) {
      DataOutputStream output = new DataOutputStream(connection.getOutputStream());

       * output.writeBytes(contentJson); output.flush(); output.close();

      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
      writer.write(contentJson);
      writer.close();
    }
    // System.out.println("resp code " + connection.getResponseCode());
    TransactionData transactionData = transactionThread.getTransactionData();
    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      jobData.setEndTime(System.currentTimeMillis());
      jobData.setExecutionStatus("success");
      // HTTP_NO_CONTENT
      // System.out.println("base strategy ka " + connection.getInputStream());
      jobData.setId(UUID.randomUUID()
          .toString());
      jobData.setParentTransactionId(transactionData.getId());
      logger.log(jobData);
      return getResponseString(connection.getInputStream());

    }
    else if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
      jobData.setEndTime(System.currentTimeMillis());
      jobData.setExecutionStatus("failure");
      jobData.setId(UUID.randomUUID()
          .toString());
      jobData.setParentTransactionId(transactionData.getId());
      logger.log(jobData);
      return null;
    }

    else {
      jobData.setExecutionStatus("FAILURE");
      if (contentJson != null) {
        jobData.setRequest(contentJson);
      }
      DataInputStream errorDataStream = new DataInputStream(connection.getErrorStream());
      StringBuilder content = new StringBuilder("");
      BufferedReader d = new BufferedReader(new InputStreamReader(errorDataStream));
      String str;
      while ((str = d.readLine()) != null) {
        content.append(str);
      }
      jobData.setResponse(content.toString());
      jobData.setId(UUID.randomUUID()
          .toString());
      jobData.setParentTransactionId(transactionData.getId());
      logger.log(jobData);
      ExceptionUtil.executeHTTPNOTOK(content, jobData);

      return null;
    }
  }

  protected String getResponseString(InputStream in) throws IOException
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

  protected HttpURLConnection prepareConnection(URL URI, String methodType) throws Exception
  {
    HttpURLConnection connection = (HttpURLConnection) URI.openConnection();
    connection.setRequestMethod(methodType);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty(CommonConstants.USER_LANGUAGE, transactionThread.getTransactionData().getLanguage());
    connection.setRequestProperty(CommonConstants.DEFAULT_LANGUAGE, Constants.DEFAULT_LANGUAGE);
    connection.connect();

    return connection;
  }*/

  public void execute(String pUrl, Object pModel) throws Exception
  {
    String mContentJson = ObjectMapperUtil.writeValueAsString(pModel);
    this.execute(pUrl, mContentJson);
  }

  public <T> T execute(String pUrl, Object pModel, Class<T> pClassType) throws Exception
  {
    String mContentJson = ObjectMapperUtil.writeValueAsString(pModel);
    return ObjectMapperUtil.readValue(this.execute(pUrl, mContentJson), pClassType);
  }

  public <T> T execute(String pUrl, Object pModel, TypeReference pClassType) throws Exception
  {
    String mContentJson = ObjectMapperUtil.writeValueAsString(pModel);
    return ObjectMapperUtil.readValue(this.execute(pUrl, mContentJson), pClassType);
  }

  private InputStream execute(String useCase, String contentJson) throws Exception
  {
    URI uri = null;
    int statusCode;
    InputStream inputStream = null;
    String mErrorResponse = null;

    JobData jobData = new JobData();
    jobData.setKlassName(this.getClass()
        .getSimpleName());
    jobData.setUseCase(useCase);
    useCase = "/" + useCase + "/" + orientDBdatabase + "/";

    if (executeWithLoadBalancer) {
      jobData.setStartTime(System.currentTimeMillis());
      CSHttpResponse httpResponse = executeWithLoadBalancer(useCase, contentJson, "POST", jobData);
      uri = ((CSHttpResponse) httpResponse).getRequestedURI();
      inputStream = httpResponse.getInputStream();
      statusCode = httpResponse.getStatus();
    }
    else {
      uri = new URI("http://" + orientDBHost + ":" + orientDBPort + useCase);
      jobData.setStartTime(System.currentTimeMillis());
      HttpURLConnection connection = executeWithoutLoadBalancer(uri, contentJson, "POST");
      statusCode = connection.getResponseCode();
      if (statusCode == HttpURLConnection.HTTP_OK) {
        inputStream = connection.getInputStream();
      }
      else {
        mErrorResponse = getResponseString(connection.getErrorStream());
      }
    }

    jobData.setRequestURL(uri.toString());
    jobData.setRequestMethod("POST");

    TransactionData transactionData = transactionThread.getTransactionData();
    InteractorData interactorData = transactionData.getInteractorData();
    
    if (statusCode == HttpURLConnection.HTTP_OK) {
      successJobData(jobData, transactionData);
      if (interactorData != null) {
        pxpLogger.taggedInfoMessage("UseCase : %s | ClassName: %s | Turn arround time: %s",
            interactorData.getKlassName(), interactorData.getKlassName(), jobData.getKlassName(),
            (jobData.getEndTime() - jobData.getStartTime()));
      } else {
        System.out.println("Unable to find interactor information in '"+ jobData.getKlassName() +"' strategy call.");
      }
    }
    else if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
      jobData.setEndTime(System.currentTimeMillis());
      jobData.setExecutionStatus("failure");
      jobData.setId(UUID.randomUUID()
          .toString());
      jobData.setParentTransactionId(transactionData.getId());
      if (transactionData.getLevel()
          .equals(0)) {
        /*   logger.log(jobData);*/
      }
    }
    else {
      failureJobData(jobData, transactionData, contentJson, mErrorResponse);
    }
    
    return inputStream;
  }

  /*@Deprecated
  public String executePlugin(String useCase, Object requestMap) throws Exception {
  	URI uri;
  	int statusCode;
  	InputStream inputStream;
  	String response;
  	JobData jobData = new JobData();
  	jobData.setKlassName(this.getClass().getSimpleName());
  	jobData.setUseCase(useCase);
  	useCase = "/" + useCase + "/" + orientDBdatabase + "/";
  	String contentJson = getRequestBodyString(requestMap);
  	if (executeWithLoadBalancer) {
  		jobData.setStartTime(System.currentTimeMillis());
  		CSHttpResponse httpResponse = executeWithLoadBalancer(useCase, contentJson, "POST", jobData);
  		uri = ((CSHttpResponse) httpResponse).getRequestedURI();
  		inputStream = httpResponse.getInputStream();
  		statusCode = httpResponse.getStatus();
  		response = getResponseString(inputStream);
  	} else {
  		uri = new URI("http://" + orientDBHost + ":" + orientDBPort + useCase);
  		jobData.setStartTime(System.currentTimeMillis());
  		HttpURLConnection connection = executeWithoutLoadBalancer(uri, contentJson, "POST");
  		statusCode = connection.getResponseCode();
  		if (statusCode == HttpURLConnection.HTTP_OK) {
  			response = getResponseString(connection.getInputStream());
  		} else {
  			response = getResponseString(connection.getErrorStream());
  		}
  	}
  	jobData.setRequestURL(uri.toString());
  	jobData.setRequestMethod("POST");
  	TransactionData transactionData = transactionThread.getTransactionData();
  	if (statusCode == HttpURLConnection.HTTP_OK) {
  		successJobData(jobData, transactionData);
  		return response;
  	} else if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
  		jobData.setEndTime(System.currentTimeMillis());
  		jobData.setExecutionStatus("failure");
  		jobData.setId(UUID.randomUUID().toString());
  		jobData.setParentTransactionId(transactionData.getId());
  		if(transactionData.getLevel().equals(0)) {
  		  logger.log(jobData);
  		}
  		return null;
  	}
  	else {
  		failureJobData(jobData, transactionData, contentJson, response);
  		return null;
  	}
  }*/

  protected String getResponseString(InputStream in) throws IOException
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

  protected HttpURLConnection prepareConnection(URL URI, String methodType) throws Exception
  {
    HttpURLConnection connection = (HttpURLConnection) URI.openConnection();
    connection.setRequestMethod(methodType);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");

    TransactionData transactionData = transactionThread.getTransactionData();
    connection.setRequestProperty(ITransactionData.UI_LANGUAGE, transactionData.getUiLanguage());
    connection.setRequestProperty(ITransactionData.DATA_LANGUAGE,
        transactionData.getDataLanguage());
    connection.connect();

    return connection;
  }

  protected CSHttpResponse executeWithLoadBalancer(String useCase, String contentJson,
      String methodType, JobData jobData) throws Exception
  {

    CSLoadBalancingModel lbModel = new CSLoadBalancingModel(useCase,
        OrientDBBaseStrategy.class.getSimpleName(), methodType, contentJson);
    ICSLoadBalancer instanceOfLB = (ICSLoadBalancer) applicationContext
        .getBean("loadBalancingStrategy");
    CSHttpResponse httpResponse = (CSHttpResponse) instanceOfLB.execute(lbModel);
    BaseLoadBalancer orientLoadBalancer = (BaseLoadBalancer) OrientDBLoadBalancer
        .getOrientLoadBalancer();
    updateConsoleLog(httpResponse.getRequestedURI(), orientLoadBalancer);

    return httpResponse;
  }

  protected HttpURLConnection executeWithoutLoadBalancer(URI uri, String contentJson,
      String methodType) throws MalformedURLException, Exception
  {
    if (methodType.equals("POST")) {
      Authenticator.setDefault(getAuthenticator);
    }
    HttpURLConnection connection = prepareConnection(uri.toURL(), methodType);
    if (contentJson != null) {
      DataOutputStream output = new DataOutputStream(connection.getOutputStream());
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
      writer.write(contentJson);
      writer.close();
    }
    updateConsoleLog(uri, null);
    return connection;
  }

  private String getRequestBodyString(Object requestMap) throws JsonProcessingException
  {
    String contentJson = ObjectMapperUtil.writeValueAsString(requestMap);
    JsonParser pareser = new JsonParser();
    pareser.parse(contentJson);
    String contentJsonEncoded = new String(contentJson);
    return contentJsonEncoded;
  }

  private void updateConsoleLog(URI URI, BaseLoadBalancer orientLoadBalancer)
  {
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      String message = "==== SERVER PLUGIN REQUEST==== URI -" + URI;
      RDBMSLogger.instance().info(message);
      
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println("\n======================= SERVER PLUGIN REQUEST======================");
      System.out.println("\nURI : " + URI);
    }
  }

  private void failureJobData(JobData jobData, TransactionData transactionData, String contentJson,
      String contentString) throws Exception
  {
    jobData.setExecutionStatus("FAILURE");
    if (contentJson != null) {
      jobData.setRequest(contentJson);
    }
    StringBuilder content = new StringBuilder(contentString);
    jobData.setResponse(content.toString());
    jobData.setId(UUID.randomUUID()
        .toString());
    jobData.setParentTransactionId(transactionData.getId());
    if (transactionData.getLevel()
        .equals(0)) {
      /*  logger.log(jobData);*/
    }
    // logTime(jobData);
    // System.out.println(content);
    ExceptionUtil.executeHTTPNOTOK(content, jobData);
  }

  private void successJobData(JobData jobData, TransactionData transactionData)
  {
    jobData.setEndTime(System.currentTimeMillis());
    jobData.setExecutionStatus("success");
    // logTime(jobData);
    jobData.setId(UUID.randomUUID()
        .toString());
    jobData.setParentTransactionId(transactionData.getId());
    if (transactionData.getLevel()
        .equals(0)) {
      /* logger.log(jobData);*/
    }
  }

  private void logTime(JobData jobData)
  {
    String usecase = getUsecase();
    if (!usecase.isEmpty() && isTimeLoggingEnabled) {
      System.out.println("\nTime Required for " + usecase + " = "
          + (jobData.getEndTime() - jobData.getStartTime()) + " ms");
    }
  }

  protected String getUsecase()
  {
    // Override this method to get time for whichever usecase it is required
    return "";
  }
}
