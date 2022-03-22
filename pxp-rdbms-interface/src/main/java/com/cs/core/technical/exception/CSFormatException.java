package com.cs.core.technical.exception;

/**
 * A specific exception marker that signals formating error
 *
 * @author vallee
 */
public class CSFormatException extends Exception {

  /**
   * Build an exception from another one
   *
   * @param message the initial message
   * @param exc the source exception
   */
  public CSFormatException(String message, Throwable exc) {
    super(message, exc);
  }

  /**
   * Build an exception from a single message
   *
   * @param message
   */
  public CSFormatException(String message) {
    super(message);
  }
}
