package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
 * CSResponseErrorHandler extends Spring's class DefaultResponseErrorHandler.
 * This method is called by response handler (csHandleResponse) in case of
 * exception. This method is mainly extended to customize error handling.
 */

public class CSResponseErrorHandler extends DefaultResponseErrorHandler {
  
  private final String exceptionOccured = "\n******************************************** Plugin/ServerException ********************************************\n";
  private final String simpleDateFormat = "E yyyy.MM.dd 'at' kk:mm:ss a zzz";
  
  public String        responseBody     = null;
  
  /*
   * Extract status code from response.
   */
  protected HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException
  {
    HttpStatus statusCode;
    try {
      statusCode = response.getStatusCode();
    }
    catch (IllegalArgumentException ex) {
      throw new UnknownHttpStatusCodeException(response.getRawStatusCode(),
          response.getStatusText(), response.getHeaders(), getResponseBody(response),
          getCharset(response));
    }
    return statusCode;
  }
  
  /*
   *
   * Handles the error in the given response. This method is only called when
   * ClientHttpResponse #hasError has returned {@code true}.
   *
   * @param response : the response recieved with the error
   *
   * @throws IOException in case of I/O errors
   *
   * @throws HttpClientErrorException : if status code is of client error
   * series, logs HttpServerErrorException : if status code is of server error
   * series
   *
   * @throws a RestClientException in other cases.
   */
  
  @Override
  public void handleError(ClientHttpResponse response) throws IOException
  {
    HttpStatus statusCode = getHttpStatusCode(response);
    switch (statusCode.series()) {
      case CLIENT_ERROR:
        HttpClientErrorException clientException = new HttpClientErrorException(statusCode,
            response.getStatusText(), response.getHeaders(), getResponseBody(response),
            getCharset(response));
        setResponseBody(responseBody);
        throw clientException;
      case SERVER_ERROR:
        String strDate = getDate();
        HttpServerErrorException serverException = new HttpServerErrorException(statusCode,
            response.getStatusText(), response.getHeaders(), getResponseBody(response),
            getCharset(response));
        String exceptionResponseBody = serverException.getResponseBodyAsString();
        setResponseBody(exceptionResponseBody);
        JsonObject obj = (JsonObject) new JsonParser().parse(exceptionResponseBody);
        JsonObject keyObj = (JsonObject) obj.get("exceptionDetails")
            .getAsJsonArray()
            .get(0);
        System.out.println(exceptionOccured);
        System.out.println(strDate + "  Caused due to " + keyObj.get("key")
            .getAsString() + "  ExceptionClass : "
            + obj.get("exceptionClass")
                .getAsString()
            + "\n");
        break;
      
      default:
        throw new RestClientException("Unknown status code [" + statusCode + "]");
    }
  }
  
  public String getResponseBodyString()
  {
    return responseBody;
  }
  
  public void setResponseBody(String responseBody)
  {
    this.responseBody = responseBody;
  }
  
  private String getDate()
  {
    Date now = new Date();
    SimpleDateFormat dateformat = new SimpleDateFormat(simpleDateFormat);
    dateformat.setTimeZone(TimeZone.getTimeZone("IST"));
    String date = dateformat.format(now);
    
    return date;
  }
  
  /*
   * Extract response body from response.
   *
   * @param response : the response recieved with the error
   */
  
  protected byte[] getResponseBody(ClientHttpResponse response)
  {
    try {
      InputStream responseBody = response.getBody();
      if (responseBody != null) {
        return FileCopyUtils.copyToByteArray(responseBody);
      }
    }
    catch (IOException ex) {
    }
    return new byte[0];
  }
  
  protected Charset getCharset(ClientHttpResponse response)
  {
    HttpHeaders headers = response.getHeaders();
    MediaType contentType = headers.getContentType();
    return contentType != null ? contentType.getCharset() : null;
  }
}
