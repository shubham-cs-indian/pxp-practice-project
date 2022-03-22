package com.cs.core.technical.rdbms.exception;

/**
 * Exception thrown by the RDBMS component. The error message is formatted with the following information: - error code - type of error -
 * error explanation
 *
 * @author vallee
 */
public class RDBMSException extends Exception {

  /**
   * @param errorCode is the code error provided in the RDBMS error catalog
   * @param errorType is the error type provided in the RDBMS error catalog
   * @param errorMessage is any contextual information added to the message
   */
  public RDBMSException(int errorCode, String errorType, String errorMessage) {
    super(String.format("%04d::%s::%s", errorCode, errorType, errorMessage));
  }

  /**
   * @param errorCode is the code error provided in the RDBMS error catalog
   * @param errorType is the error type provided in the RDBMS error catalog
   * @param sourceException is the technical exception at the origin of the error
   */
  public RDBMSException(int errorCode, String errorType, Throwable sourceException) {
    super(String.format("%04d::%s->%s", errorCode, errorType, sourceException.getMessage(),
            sourceException));
  }
}
