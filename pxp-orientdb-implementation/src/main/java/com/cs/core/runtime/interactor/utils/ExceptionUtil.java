package com.cs.core.runtime.interactor.utils;

import java.util.List;
import java.util.Map;

import com.cs.core.exception.PluginException;
import com.cs.core.exception.UnknownPluginException;
import com.cs.core.runtime.interactor.model.logger.IJobData;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IPluginExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.PluginExceptionModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

public class ExceptionUtil {
  
  @SuppressWarnings("unchecked")
  public static void executeHTTPNOTOK(StringBuilder content, IJobData reference) throws Exception
  {
    Map<String, Object> responseMap = ObjectMapperUtil.readValue(content.toString(), Map.class);
    String exceptionClass = (String) responseMap.get("exceptionClass");
    
    if (exceptionClass == null) {
      throw new UnknownPluginException();
    }
    else {
      PluginExceptionModel pluginExceptionModel = ObjectMapperUtil.readValue(content.toString(),
          PluginExceptionModel.class);
      Exception exceptionObject = null;
      try {
        exceptionObject = (Exception) Class.forName(exceptionClass)
            .newInstance();
      }
      catch (Exception e) {
        UnknownPluginException ex = new UnknownPluginException(e.getClass()
            .getName() + " : " + e.getMessage());
        ex.setExceptionDetails(pluginExceptionModel.getExceptionDetails());
        ex.setDevExceptionDetails(pluginExceptionModel.getDevExceptionDetails());
        throw ex;
      }
      List<IDevExceptionDetailModel> devExceptionDetails = pluginExceptionModel
          .getDevExceptionDetails();
      if (exceptionObject instanceof PluginException) {
        PluginException pluginException = (PluginException) exceptionObject;
        pluginException.setExceptionDetails(pluginExceptionModel.getExceptionDetails());
        pluginException.setDevExceptionDetails(devExceptionDetails);
      }
      
      // TODO: if not Bulk Exception
      if (devExceptionDetails.size() == 1) {
        exceptionObject.setStackTrace(devExceptionDetails.get(0)
            .getStack());
      }
      
      throw exceptionObject;
    }
  }
  
  public static void handleExceptionKafkaResponse(IPluginExceptionModel pluginExceptionModel)
      throws Exception
  {
    String exceptionClass = (String) ((PluginExceptionModel) pluginExceptionModel)
        .getExceptionClass();
    
    if (exceptionClass == null) {
      throw new UnknownPluginException();
    }
    else {
      Exception exceptionObject = null;
      try {
        exceptionObject = (Exception) Class.forName(exceptionClass)
            .newInstance();
      }
      catch (Exception e) {
        UnknownPluginException ex = new UnknownPluginException(e.getClass()
            .getName() + " : " + e.getMessage());
        ex.setExceptionDetails(pluginExceptionModel.getExceptionDetails());
        ex.setDevExceptionDetails(pluginExceptionModel.getDevExceptionDetails());
        throw ex;
      }
      
      List<IDevExceptionDetailModel> devExceptionDetails = ((PluginExceptionModel) pluginExceptionModel)
          .getDevExceptionDetails();
      if (exceptionObject instanceof PluginException) {
        PluginException pluginException = (PluginException) exceptionObject;
        pluginException.setExceptionDetails(
            ((PluginExceptionModel) pluginExceptionModel).getExceptionDetails());
        pluginException.setDevExceptionDetails(devExceptionDetails);
      }
      
      // TODO: if not Bulk Exception
      if (devExceptionDetails.size() == 1) {
        exceptionObject.setStackTrace(devExceptionDetails.get(0)
            .getStack());
      }
      
      throw exceptionObject;
    }
  }
  
  @SuppressWarnings("unchecked")
  public static void handleExceptionOracleResponse(String errorString) throws Exception
  {
    Map<String, Object> responseMap = ObjectMapperUtil.readValue(errorString, Map.class);
    String exceptionClass = (String) responseMap.get("exceptionClass");
    
    if (exceptionClass == null) {
      throw new UnknownPluginException();
    }
    else {
      PluginExceptionModel pluginExceptionModel = ObjectMapperUtil.readValue(errorString,
          PluginExceptionModel.class);
      Exception exceptionObject = null;
      try {
        exceptionObject = (Exception) Class.forName(exceptionClass)
            .newInstance();
      }
      catch (Exception e) {
        UnknownPluginException ex = new UnknownPluginException(e.getClass()
            .getName() + " : " + e.getMessage());
        ex.setExceptionDetails(pluginExceptionModel.getExceptionDetails());
        ex.setDevExceptionDetails(pluginExceptionModel.getDevExceptionDetails());
        throw ex;
      }
      List<IDevExceptionDetailModel> devExceptionDetails = pluginExceptionModel
          .getDevExceptionDetails();
      if (exceptionObject instanceof PluginException) {
        PluginException pluginException = (PluginException) exceptionObject;
        pluginException.setExceptionDetails(pluginExceptionModel.getExceptionDetails());
        pluginException.setDevExceptionDetails(devExceptionDetails);
      }
      
      // TODO: if not Bulk Exception
      if (devExceptionDetails.size() == 1) {
        exceptionObject.setStackTrace(devExceptionDetails.get(0)
            .getStack());
      }
      
      throw exceptionObject;
    }
  }
}
