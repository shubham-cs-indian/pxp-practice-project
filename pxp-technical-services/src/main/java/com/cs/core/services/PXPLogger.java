package com.cs.core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.technical.exception.CSInitializationException;

@Component
public class PXPLogger extends CSLogger {
  
  @Value("${pxplog.isUseCaseBaseLogEnabled}") 
  boolean isUseCaseBaseLogEnabled;
  
  protected PXPLogger()
      throws CSInitializationException
  {
    super();
  }
  
  @Override
  public void trace(String message)
  {
    super.trace(message);
  }
  
  @Override
  public void trace(String format, Object... data)
  {
    super.trace(format, data);
  }
  
  @Override
  public void taggedTraceMessage(String message, String tagName)
  {
    super.taggedTraceMessage(message, tagName);
  }
  
  @Override
  public void taggedTraceMessage(String message, String tagName, Object... data)
  {
    if (isUseCaseBaseLogEnabled) {
      super.taggedTraceMessage(message, tagName, data);
    } 
  }
  
  @Override
  public void debug(String message)
  {
    super.debug(message);
  }
  
  @Override
  public void debug(String format, Object... data)
  {
    super.debug(format, data);
  }
  
  @Override
  public void taggedDebugMessage(String message, String tagName)
  {
    super.taggedDebugMessage(message, tagName);
  }
  
  @Override
  public void taggedDebugMessage(String message, String tagName, Object... data)
  {
    if (isUseCaseBaseLogEnabled) {
      super.taggedDebugMessage(message, tagName, data);
    } 
  }
  
  @Override
  public void info(String message)
  {
    super.info(message);
  }
  
  @Override
  public void info(String format, Object... data)
  {
    super.info(format, data);
  }
  
  @Override
  public void taggedInfoMessage(String message, String tagName)
  {
    super.taggedInfoMessage(message, tagName);
  }
  
  @Override
  public void taggedInfoMessage(String message, String tagName, Object... data)
  {
    if (isUseCaseBaseLogEnabled) {
      super.taggedInfoMessage(message, tagName, data);
    } 
  }
  
  @Override
  public void warn(String message)
  {
    super.warn(message);
  }
  
  @Override
  public void warn(String format, Object... data)
  {
    super.warn(format, data);
  }
  
  @Override
  public void taggedWarnMessage(String message, String tagName)
  {
    super.taggedWarnMessage(message, tagName);
  }
  
  @Override
  public void taggedWarnMessage(String message, String tagName, Object... data)
  {
    if (isUseCaseBaseLogEnabled) {
      super.taggedWarnMessage(message, tagName, data);
    } 
  }
  
  @Override
  public void exception(Throwable exc)
  {
    super.exception(exc);
  }
  
  @Override
  public void taggedException(Throwable exc, String tagName)
  {
    super.taggedException(exc, tagName);
  }
  
}
