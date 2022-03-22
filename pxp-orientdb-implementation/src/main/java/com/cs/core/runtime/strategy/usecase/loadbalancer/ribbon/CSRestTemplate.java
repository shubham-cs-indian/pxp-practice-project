package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.*;

import javax.xml.crypto.URIReferenceException;
import java.net.URI;

/*
 * Extends Spring's central class RestTemplate for synchronous client-side HTTP
 * access. The main entry points of this class is execute. This method is mainly
 * extended to customize error handling
 */

public class CSRestTemplate extends RestTemplate {
  
  private CSResponseErrorHandler errorHandler = new CSResponseErrorHandler();
  
  @Override
  public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback,
      ResponseExtractor<T> responseExtractor) throws RestClientException
  {
    
    try {
      return csDoExecute(url, method, requestCallback, responseExtractor);
    }
    catch (Exception e) {
      throw new RestClientException(e.getMessage(), e);
    }
  }
  
  @SuppressWarnings("unchecked")
  protected <T> T csDoExecute(URI url, HttpMethod method, RequestCallback requestCallback,
      ResponseExtractor<T> responseExtractor) throws Exception
  {
    
    if (url != null) {
      if (method != null) {
        ClientHttpResponse response = null;
        String errorResponse = null;
        try {
          ClientHttpRequest request = createRequest(url, method);
          if (requestCallback != null) {
            requestCallback.doWithRequest(request);
          }
          response = request.execute();
          errorResponse = csHandleErrorResponse(url, method, response);
          
          if (errorResponse == null) {
            if (responseExtractor != null) {
              return responseExtractor.extractData(response);
            }
            else {
              // TODO: Test this case
              return null;
            }
          }
          else {
            
            return (T) new ResponseEntity<String>(errorResponse, response.getHeaders(),
                response.getStatusCode());
          }
          
        }
        // TODO: Handle connection timeouts specific exception
        catch (Exception ex) {
          throw new ResourceAccessException(url + " : " + ex.getMessage());
        }
        finally {
          if (response != null) {
            response.close();
          }
        }
      }
      else {
        
        throw new Exception("Http method cannot be null");
      }
    }
    else {
      throw new URIReferenceException();
    }
  }
  
  protected String csHandleErrorResponse(URI url, HttpMethod method, ClientHttpResponse response)
      throws Exception
  {
    CSResponseErrorHandler errorHandler = getErrorHandler();
    boolean hasError = errorHandler.hasError(response);
    if (hasError) {
      errorHandler.handleError(response);
      return errorHandler.getResponseBodyString();
      
    }
    else {
      return null;
    }
  }
  
  /*
   * private String getResponseBodyAsString(InputStream in) throws IOException
   * { StringBuilder content = new StringBuilder(); if (in != null) {
   * in.reset(); BufferedReader d = new BufferedReader(new
   * InputStreamReader(in, "UTF-8")); String str; while ((str = d.readLine())
   * != null) { content.append(str); } } return content.toString(); }
   */
  @Override
  public CSResponseErrorHandler getErrorHandler()
  {
    return this.errorHandler;
  }
}
