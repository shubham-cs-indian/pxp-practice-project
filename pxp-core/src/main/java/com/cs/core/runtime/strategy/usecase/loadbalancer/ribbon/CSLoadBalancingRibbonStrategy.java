package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.model.loadbalancer.CSLoadBalancingModel;
import com.cs.core.runtime.strategy.usecase.loadbalancer.ICSLoadBalancer;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpRequest.Builder;
import com.netflix.client.http.HttpRequest.Verb;
import com.netflix.client.http.HttpResponse;

@Component
public class CSLoadBalancingRibbonStrategy implements ICSLoadBalancer {
  
  public static final String      AUTHENTICATION_TYPE                      = "Basic";
  public static final String      AUTHENTICATION_CREDENTIALS               = "root:pass@123";
  static String                   encodevalue                              = Base64.getEncoder()
      .encodeToString(AUTHENTICATION_CREDENTIALS.getBytes());
  public static final String      AUTHENTICATION_ENCODED_CREDENTIALS_VALUE = AUTHENTICATION_TYPE
      + " " + encodevalue;
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Override
  public IModel execute(IModel model) throws Exception
  {
    /*CSLoadBalancingModel lbModel = (CSLoadBalancingModel) model;
    if (lbModel.getInitiatingStrategyName()
        .equals("BaseElasticStrategy")) {
      CSLoadBalancerAwareClient elasticClient = ElasticLoadBalancer.getElasticClient();
      String url = lbModel.getURL();
      String contentJson = lbModel.getRequestContent();
      String methodType = lbModel.getMethodType();
      HttpRequest httpRequest = createRequest(url, contentJson, methodType, "elastic");
      HttpResponse httpResponse = elasticClient.executeWithLoadBalancer(httpRequest);
      return (CSHttpResponse) httpResponse;
    }
    else if (lbModel.getInitiatingStrategyName()
        .equals("OrientDBBaseStrategy")) {
      CSLoadBalancerAwareClient orientClient = OrientDBLoadBalancer.getOrientClient();
      String useCase = lbModel.getURL();
      String contentJson = lbModel.getRequestContent();
      String methodType = lbModel.getMethodType();
      HttpRequest httpRequest = createRequest(useCase, contentJson, methodType, "orient");
      HttpResponse httpResponse = orientClient.executeWithLoadBalancer(httpRequest);
      
      return (CSHttpResponse) httpResponse;
    }*/
    
    return null;
  }
  
  protected HttpRequest createRequest(String url, String contentJson, String methodType,
      String requestingClient)
  {
    HttpRequest request = null;
    Verb httpMethod = getHttpMethodType(methodType);
    Builder requestBuilder;
    try {
      requestBuilder = HttpRequest.newBuilder()
          .verb(httpMethod)
          .uri(new URI(url))
          .setRetriable(true);
      requestBuilder.header("Content-Type", "application/json");
      
      requestBuilder.header(ITransactionData.DATA_LANGUAGE, transactionThread.getTransactionData()
          .getDataLanguage());
      if (contentJson != null && !(contentJson.isEmpty())) {
        requestBuilder.entity(contentJson);
      }
      if (requestingClient.equals("orient")) {
        requestBuilder.header(CommonConstants.UI_LANGUAGE, transactionThread.getTransactionData()
            .getUiLanguage());
        requestBuilder.header("authorization", AUTHENTICATION_ENCODED_CREDENTIALS_VALUE);
      }
      request = requestBuilder.build();
    }
    catch (URISyntaxException e) {
      RDBMSLogger.instance().exception(e);
    }
    return request;
  }
  
  private Verb getHttpMethodType(String methodType)
  {
    switch (methodType) {
      case "GET":
        return Verb.GET;
      case "POST":
        return Verb.POST;
      case "DELETE":
        return Verb.DELETE;
      case "PUT":
        return Verb.PUT;
    }
    
    return null;
  }
}
