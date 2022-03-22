package com.cs.core.services;

import static java.lang.Integer.min;

import java.util.HashMap;
import java.util.Map;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;
import com.cs.core.services.CSProperties;

import com.cs.core.technical.exception.CSInitializationException;


/**
 * CS encapsulation of usual logger functions - light logger library - - log level standardization - log message standardization
 *
 * @author vallee
 */
public class CSLogger  {

  protected static final Map<String, Integer> STACK_DEPTH_MAP = new HashMap();
  private String logLevel = "OFF";
  
  static {
    STACK_DEPTH_MAP.put("OFF", 0);
    STACK_DEPTH_MAP.put("TRACE", 50);
    STACK_DEPTH_MAP.put("DEBUG", 20);
    STACK_DEPTH_MAP.put("INFO", 10);
    STACK_DEPTH_MAP.put("WARNING", 10);
    STACK_DEPTH_MAP.put("ERROR", 5);
  }

  /**
   * The CSLogger is only accessible to subclass that may implement a singleton per PXP component All logger characteristics (including log
   * directory) are taken from the current properties So as prerequisites of using the CSLogger, CSProperties must be initialized
   *
   * @throws CSInitializationException in case of initialization error
   * @see CSProperties
   */
  protected CSLogger() throws CSInitializationException {
    logLevel = CSProperties.instance().getString("log.level");
  }

  /**
   * @param message to log
   * @return decorated message for log
   */
  private String format(String message) {
    StringBuilder fMessage = new StringBuilder(String.format("%04X", Thread.currentThread().getId()));
    fMessage.append("|").append(message);
    return fMessage.toString();
  }

  /**
   * @param message of trace level
   */
  public void trace(String message) {
    Logger.trace(format(message));
  }

  /**
   * @param format String format to log at INFO level
   * @param data format parameter(s)
   */
  public void trace(String format, Object... data) {
    trace(String.format(format, data));
  }
  
  /**
   *  @param message of trace level
   *  @param tag name to identify writer.
   */
  public void taggedTraceMessage(String message, String tagName) {
    Logger.tag(tagName).trace(format(message));
  }
  
  /**
   *  @param message of trace level
   *  @param tag name to identify writer.
   *  @param data format parameter(s)
   */
  public void taggedTraceMessage(String message, String tagName, Object... data) {
    Logger.tag(tagName).trace(String.format(message, data));
  }

  /**
   * @param message of debug level
   */
  public void debug(String message) {
    Logger.debug(format(message));
  }

  /**
   * @param format String format to log at DEBUG level
   * @param data format parameter(s)
   */
  public void debug(String format, Object... data) {
    debug(String.format(format, data));
  }
  
  /**
   *  @param message of debug level
   *  @param tag name to identify writer.
   */
  public void taggedDebugMessage(String message, String tagName) {
    Logger.tag(tagName).debug(format(message));
  }
  
  /**
   *  @param message of debug level
   *  @param tag name to identify writer.
   *  @param data format parameter(s)
   */
  public void taggedDebugMessage(String message, String tagName, Object... data) {
    Logger.tag(tagName).debug(String.format(message, data));
  }

  /**
   * @param message of info level
   */
  public void info(String message) {
    Logger.info(format(message));
  }

  /**
   * @param format String format to log at INFO level
   * @param data format parameter(s)
   */
  public void info(String format, Object... data) {
    info(String.format(format, data));
  }
  
  /**
   *  @param message of info level
   *  @param tag name to identify writer.
   */
  public void taggedInfoMessage(String message, String tagName) {
    Logger.tag(tagName).info(format(message));
  }
  
  /**
   *  @param message of info level
   *  @param tag name to identify writer.
   *  @param data format parameter(s)
   */
  public void taggedInfoMessage(String message, String tagName, Object... data) {
    Logger.tag(tagName).info(String.format(message, data));
  }

  /**
   * @param message of warning level
   */
  public void warn(String message) {
    Logger.warn(format(message));
  }

  /**
   * @param format String format to log at WARNING level
   * @param data format parameter(s)
   */
  public void warn(String format, Object... data) {
    warn(String.format(format, data));
  }
  
  /**
   *  @param message of warning level
   *  @param tag name to identify writer.
   */
  public void taggedWarnMessage(String message, String tagName) {
    Logger.tag(tagName).warn(format(message));
  }
  
  /**
   *  @param message of warning level
   *  @param tag name to identify writer.
   *  @param data format parameter(s)
   */
  public void taggedWarnMessage(String message, String tagName, Object... data) {
    Logger.tag(tagName).warn(String.format(message, data));
  }
  
  /**
   * @param exc an exception
   * @return the upper part of the exception's stack trace
   */
  public String getExceptionStack(Throwable exc) {
    
    StackTraceElement[] elements = exc.getStackTrace();
    if (elements.length > 0) {
      int maxStackDepth = min(STACK_DEPTH_MAP.get(logLevel), elements.length);
      StringBuilder stackTrace = new StringBuilder(elements[0].toString());
      for (int elementCount = 1; elementCount < maxStackDepth; elementCount++) {
        stackTrace.append("\n").append(elements[elementCount].toString());
      }
      return stackTrace.toString();
      
    }
    return "";
  }
  
  /**
   * Below function takes tag name/tag value for a writer and returns the level for the respective writer. 
   * @param tagName tag name of a writer.
   * @return level for the writer.
   */
  public String getLevelForWriter(String tagValue) {
    String level = "";
    Map<String, String> writers = Configuration.getSiblings("writer");
    for(Map.Entry<String,String> entry : writers.entrySet()) {
      Map<String, String> writer = Configuration.getChildren(entry.getKey());
      String writerTagValue = writer.get("tag");
      if (writerTagValue == tagValue ) {
        level = writer.get("level");
      }
    }
    return level;
  }

  /**
   * @param exc exception logged at error level
   */
  public void exception(Throwable exc) {
    Logger.error(exc, format(exc.getMessage()));
  }
  
  /**
   *  @param exc exception logged at error level
   *  @param tag name to identify writer.
   */
  public void taggedException(Throwable exc, String tagName) {
    Logger.tag(tagName).error(exc, format(exc.getMessage()));
  }

  /**
   * Create, log and throw a new application exception
   *
   * @param <E> the type of exception thrown by this method
   * @param exception the exception to be thrown
   * @throws E
   */
  public <E extends Throwable> void throwException(E exception) throws E {
    exception(exception);
    throw exception;
  }
}
