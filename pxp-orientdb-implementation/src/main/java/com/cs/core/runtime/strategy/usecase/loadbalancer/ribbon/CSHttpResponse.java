package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.google.common.reflect.TypeToken;
import com.netflix.client.ClientException;
import com.netflix.client.http.HttpHeaders;
import com.netflix.client.http.HttpResponse;

/**
 * CSHttpResponse implements interface HttpResponse.CSLoadBalancerAwareClient
 * Response recieved for HTTP communication.
 */
public class CSHttpResponse implements HttpResponse, IModel {
  
  /**
   *
   */
  private static final long   serialVersionUID = 1L;
  
  protected ResponseEntity<?> responseEntity;
  protected URI               requestedURI;
  protected InputStream       entityAsInputStream;
  protected Exception         exception;
  
  public CSHttpResponse(ResponseEntity<?> responseEntity, URI requestedURI)
  {
    this.responseEntity = responseEntity;
    this.requestedURI = requestedURI;
  }
  
  @Override
  public Object getPayload() throws ClientException
  {
    return responseEntity.getBody();
  }
  
  @Override
  public boolean hasPayload()
  {
    return responseEntity.hasBody();
  }
  
  @Override
  public boolean isSuccess()
  {
    return responseEntity.getStatusCode()
        .equals(HttpStatus.OK);
  }
  
  @Override
  public URI getRequestedURI()
  {
    return requestedURI;
  }
  
  @Override
  public int getStatus()
  {
    return responseEntity.getStatusCode()
        .value();
  }
  
  @Override
  public String getStatusLine()
  {
    return responseEntity.getStatusCode()
        .getReasonPhrase();
  }
  
  @Override
  public Map<String, Collection<String>> getHeaders()
  {
    return null;
  }
  
  @Override
  public HttpHeaders getHttpHeaders()
  {
    return (HttpHeaders) responseEntity.getHeaders();
  }
  
  @Override
  public void close()
  {
    responseEntity = null;
  }
  
  @Override
  public InputStream getInputStream()
  {
    try {
      this.entityAsInputStream = IOUtils.toInputStream(responseEntity.getBody()
          .toString(), "UTF-8");
      return entityAsInputStream;
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    return null;
  }
  
  @Override
  public boolean hasEntity()
  {
    return (responseEntity == null) ? false : true;
  }
  
  @Override
  public <T> T getEntity(Class<T> type) throws Exception
  {
    return null;
  }
  
  @Override
  public <T> T getEntity(Type type) throws Exception
  {
    return null;
  }
  
  @Override
  public <T> T getEntity(TypeToken<T> type) throws Exception
  {
    return null;
  }
}
