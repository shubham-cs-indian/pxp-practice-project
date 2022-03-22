package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.DevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ExceptionUtil {
  
  public static String stackTraceToString(Throwable e)
  {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      sb.append(element.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
  
  public static StackTraceElement[] stringToStackTrace(String stackTrace)
  {
    String[] traceElementsStr = stackTrace.split("\n");
    StackTraceElement[] traceElements = new StackTraceElement[traceElementsStr.length];
    int i = 0;
    for (String trace : traceElementsStr) {
      traceElements[i++] = getStackTraceElement(trace);
    }
    return traceElements;
  }
  
  protected static StackTraceElement getStackTraceElement(String trace)
  {
    String declaringClass = null;
    String methodName = null;
    String fileName = null;
    String lineNumberStr = null;
    int lineNumber = 0;
    
    int lastCharacter = trace.lastIndexOf(")");
    int colonCharacter = trace.lastIndexOf(":");
    int openingBraceCharacter = trace.lastIndexOf("(");
    lineNumberStr = trace.substring(colonCharacter + 1, lastCharacter);
    fileName = trace.substring(openingBraceCharacter + 1, colonCharacter);
    
    String tempName = trace.substring(0, openingBraceCharacter);
    int lastIndexOfDot = tempName.lastIndexOf(".");
    methodName = tempName.substring(lastIndexOfDot + 1);
    declaringClass = tempName.substring(0, lastIndexOfDot);
    
    try {
      lineNumber = Integer.parseInt(lineNumberStr);
    }
    catch (Throwable e) {
      
    }
    StackTraceElement stackTraceElement = new StackTraceElement(declaringClass, methodName,
        fileName, lineNumber);
    
    return stackTraceElement;
  }
  
  public static void addFailureDetailsToFailureObject(IExceptionModel failure, PluginException e,
      String itemId, String itemName)
  {
    IExceptionDetailModel exceptionDetail = e.getExceptionDetails()
        .get(0);
    IDevExceptionDetailModel devExceptionDetail = e.getDevExceptionDetails()
        .get(0);
    exceptionDetail.setItemId(itemId);
    exceptionDetail.setItemName(itemName);
    devExceptionDetail.setItemId(itemId);
    devExceptionDetail.setItemName(itemName);
    failure.getExceptionDetails()
        .add(exceptionDetail);
    failure.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
  
  public static void setFailureDetailsToFailureObject(IExceptionModel failure, PluginException e,
      String itemId, String itemName, Map<String, String> properties)
  {
    IExceptionDetailModel exceptionDetail = e.getExceptionDetails()
        .get(0);
    IDevExceptionDetailModel devExceptionDetail = e.getDevExceptionDetails()
        .get(0);
    exceptionDetail.setItemId(itemId);
    exceptionDetail.setItemName(itemName);
    exceptionDetail.setProperties(properties);
    devExceptionDetail.setItemId(itemId);
    devExceptionDetail.setItemName(itemName);
    failure.getExceptionDetails()
        .add(exceptionDetail);
    failure.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
  
  public static void addFailureDetailsToFailureObject(IExceptionModel failure, Exception e,
      String itemId, String itemName)
  {
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(e.getClass()
        .getSimpleName());
    exceptionDetail.setItemId(itemId);
    exceptionDetail.setItemName(itemName);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setDetailMessage(e.getMessage());
    devExceptionDetail.setKey(e.getClass()
        .getSimpleName());
    devExceptionDetail.setStack(e.getStackTrace());
    devExceptionDetail.setExceptionClass(e.getClass()
        .getName());
    devExceptionDetail.setItemId(itemId);
    devExceptionDetail.setItemName(itemName);
    
    failure.getExceptionDetails()
        .add(exceptionDetail);
    failure.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
  
  public static Exception getExceptionFromBuildFailureResponse(PluginException exception)
      throws Exception
  {
    List<IDevExceptionDetailModel> devExceptionDetails = exception.getDevExceptionDetails();
    if (devExceptionDetails != null && devExceptionDetails.size() == 1) {
      IDevExceptionDetailModel devExceptionDetail = devExceptionDetails.get(0);
      String exceptionClass = devExceptionDetail.getExceptionClass();
      Exception exceptionObject = (Exception) Class.forName(exceptionClass)
          .newInstance();
      
      if (exceptionObject instanceof PluginException) {
        PluginException pluginException = (PluginException) exceptionObject;
        pluginException.setExceptionDetails(exception.getExceptionDetails());
        pluginException.setDevExceptionDetails(devExceptionDetails);
      }
      exceptionObject.setStackTrace(devExceptionDetail.getStack());
      
      return exceptionObject;
    }
    
    throw new UnknownPluginException();
  }
  
  public static void replaceExceptionKeyWithSpecificExceptionKey(Properties exceptionMap,
      List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    for (int i = 0; i < exceptionDetails.size(); i++) {
      IExceptionDetailModel exceptionDetail = exceptionDetails.get(i);
      String key = exceptionMap.getProperty(exceptionDetail.getKey());
      if (key != null) {
        exceptionDetail.setKey(key);
      }
      
      IDevExceptionDetailModel devExceptionDetail = devExceptionDetails.get(i);
      key = exceptionMap.getProperty(devExceptionDetail.getKey());
      if (key != null) {
        devExceptionDetail.setKey(key);
      }
    }
  }
  
  public static PluginException getInstanceNotFoundException(PluginException exception,
      String itemId, String itemName) throws Exception
  {
    exception.getExceptionDetails()
        .get(0)
        .setItemId(itemId);
    exception.getExceptionDetails()
        .get(0)
        .setItemName(itemName);
    exception.getDevExceptionDetails()
        .get(0)
        .setItemId(itemId);
    exception.getDevExceptionDetails()
        .get(0)
        .setItemName(itemName);
    
    return exception;
  }
}
