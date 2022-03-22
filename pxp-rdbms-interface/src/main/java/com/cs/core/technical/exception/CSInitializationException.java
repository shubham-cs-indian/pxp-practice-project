package com.cs.core.technical.exception;

/**
 * A specific exception marker that signals initialization error
 *
 * @author vallee
 */
public class CSInitializationException extends Exception {

  /**
   * Build an exception from another one
   *
   * @param message the initial message
   * @param exc the source exception
   */
  public CSInitializationException(String message, Throwable exc) {
    super(message, exc);
  }

  /**
   * Build an exception from a single message
   *
   * @param message
   */
  public CSInitializationException(String message) {
    super(message);
  }
}
