package com.cs.core.rdbms.tracking.idao;

/**
 * Access to the logger function of the RDBMS component
 *
 * @author vallee
 */
public interface ILoggerDAO {

  /**
   * @param message to log at TRACE level
   */
  public void trace(String message);

  /**
   * @param format String format to log at TRACE level
   * @param data format parameter(s)
   */
  public void trace(String format, Object... data);

  /**
   * @param message to log at DEBUG level
   */
  public void debug(String message);

  /**
   * @param format String format to log at DEBUG level
   * @param data format parameter(s)
   */
  public void debug(String format, Object... data);

  /**
   * @param message to log at INFO level
   */
  public void info(String message);

  /**
   * @param format String format to log at INFO level
   * @param data format parameter(s)
   */
  public void info(String format, Object... data);

  /**
   * @param message to log at WARNING level
   */
  public void warn(String message);

  /**
   * @param format String format to log at WARNING level
   * @param data format parameter(s)
   */
  public void warn(String format, Object... data);

  /**
   * @param exc is the logged exception
   */
  public void exception(Throwable exc);
}
