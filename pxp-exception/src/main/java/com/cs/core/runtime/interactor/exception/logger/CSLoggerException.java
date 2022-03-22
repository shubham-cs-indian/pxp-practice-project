package com.cs.core.runtime.interactor.exception.logger;

public class CSLoggerException extends RuntimeException {
  
  private static final long  serialVersionUID     = 3781438265448874734L;
  
  public static final String CS_LOGGER_ERROR_CODE = "00";
  
  private String             errorCode;
  
  public CSLoggerException(Exception e)
  {
    super(e);
    this.errorCode = CS_LOGGER_ERROR_CODE + "00";
  }
  
  public CSLoggerException(String message, String errorCode)
  {
    super(message);
    this.errorCode = CS_LOGGER_ERROR_CODE + errorCode;
  }
  
  public String getErrorCode()
  {
    return this.errorCode;
  }
  
  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }
}
