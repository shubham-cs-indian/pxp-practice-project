package com.cs.core.bgprocess.dto;

import com.cs.core.rdbms.driver.RDBMSLogger;

/**
 * BGP job logging capabilities
 *
 * @author vallee
 */
public class BGPLog {
  
  public enum Status
  {
    PROGRESS, INFO, WARN, ERROR, EXCEPTION;
  }
  
  private final BGPSummaryDTO processSummary;  // The summary to be updated with log
  private StringBuffer        logContent = new StringBuffer();
  
  /**
   * Build a new empty log
   *
   * @param summary
   */
  BGPLog(BGPSummaryDTO summary)
  {
    processSummary = summary;
  }
  
  /**
   * Build a log from an existing content
   *
   * @param summary
   * @param log
   */
  BGPLog(BGPSummaryDTO summary, String log)
  {
    processSummary = summary;
    logContent = new StringBuffer(log);
  }
  
  /**
   * Internal formatter of log messages
   *
   * @param status
   * @param message
   * @return
   */
  private String format(Status status, String message)
  {
    return String.format("\n%s:%s", status.toString(), message);
  }
  
  /**
   * set a progress message
   *
   * @param message
   */
  public void progress(String message)
  {
    processSummary.incNbOfLogLines();
    logContent.append(format(Status.PROGRESS, message));
  }
  
  /**
   * set a progress message with free formatting
   *
   * @param messageFormat
   * @param var
   */
  public void progress(String messageFormat, Object... var)
  {
    progress(String.format(messageFormat, var));
  }
  
  /**
   * set an information message
   *
   * @param message
   */
  public void info(String message)
  {
    processSummary.incNbOfLogLines();
    logContent.append(format(Status.INFO, message));
  }
  
  /**
   * set an information message with free formatting
   *
   * @param messageFormat
   * @param var
   */
  public void info(String messageFormat, Object... var)
  {
    info(String.format(messageFormat, var));
  }
  
  /**
   * set a warning message
   *
   * @param message
   */
  public void warn(String message)
  {
    processSummary.incNbOfLogLines();
    processSummary.incNbOfWarning();
    logContent.append(format(Status.WARN, message));
  }
  
  /**
   * set a warning message with free formatting
   *
   * @param messageFormat
   * @param var
   */
  public void warn(String messageFormat, Object... var)
  {
    warn(String.format(messageFormat, var));
  }
  
  /**
   * set an error message
   *
   * @param message
   */
  public void error(String message)
  {
    processSummary.incNbOfLogLines();
    processSummary.incNbOfErrors();
    logContent.append(format(Status.ERROR, message));
  }
  
  /**
   * set an error message with free formatting
   *
   * @param messageFormat
   * @param var
   */
  public void error(String messageFormat, Object... var)
  {
    error(String.format(messageFormat, var));
  }
  
  /**
   * set an exception message
   *
   * @param exc
   */
  public void exception(Throwable exc)
  {
    processSummary.incNbOfLogLines();
    processSummary.incNbOfErrors();
    logContent.append(format(Status.EXCEPTION, exc.getMessage()));
    logContent.append("\n").append(RDBMSLogger.instance().getExceptionStack(exc)).append("\n");
  }
  
  /**
   * @return the current content of the log
   */
  public byte[] toBytes()
  {
    return logContent.toString().getBytes();
  }
  
  /**
   * @return the current content of the log as string
   */
  public String toString()
  {
    return logContent.toString();
  }
}
